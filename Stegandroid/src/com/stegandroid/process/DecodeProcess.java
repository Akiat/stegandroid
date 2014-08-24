package com.stegandroid.process;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.stegandroid.algorithms.AlgorithmFactory;
import com.stegandroid.algorithms.ICryptographyAlgorithm;
import com.stegandroid.algorithms.ISteganographyContainer;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.mp4.MP4MediaReader;
import com.stegandroid.parameters.DecodeParameters;

public class DecodeProcess {

	private final String DEFAULT_H264_CONTAINER = "com.stegandroid.algorithms.steganography.video.H264SteganographyContainer";
	private final String DEFAULT_AAC_CONTAINER = "com.stegandroid.algorithms.steganography.audio.AACSteganographyContainer";

	private MP4MediaReader _mp4MediaReader;
	private ISteganographyContainer _h264SteganographyContainer;
	private ISteganographyContainer _aacSteganographyContainer;	
	private ICryptographyAlgorithm _cryptographyAlgorithm;

	private byte[] _unHideData;

	public DecodeProcess() {
		_mp4MediaReader = null;
		_h264SteganographyContainer = null;
		_aacSteganographyContainer = null;
		_cryptographyAlgorithm = null;
		_unHideData = null;
	}

	public boolean decode(DecodeParameters parameters) {
		byte unHideDataVideo[] = null;
		byte unHideDataAudio[] = null;
		
		if (!init(parameters))
			return false;

		if (Preferences.getInstance().getUseVideoChannel()) {
			_h264SteganographyContainer.unHideData();
			unHideDataVideo = _h264SteganographyContainer.getUnHideData();
		} 
		if (Preferences.getInstance().getUseAudioChannel()) {
			_aacSteganographyContainer.unHideData();
			unHideDataAudio = _aacSteganographyContainer.getUnHideData();
		} 

		processContentWithCryptography(parameters, unHideDataAudio);
		processContentWithCryptography(parameters, unHideDataVideo);		
		unshuffleUnhideContent(unHideDataAudio, unHideDataVideo);
		
		if (_unHideData != null && _unHideData.length > 0) {						
			this.finalise(parameters);
			return true;
		}
		return false;
	}

	// Private methods
	// Init methods
	private boolean initCryptographyAlgorithm() {
		Preferences pref = Preferences.getInstance();
		boolean ret = true;

		if (pref.getUseCryptography()) {
			_cryptographyAlgorithm = AlgorithmFactory.getCryptographyAlgorithmInstanceFromName(pref.getCryptographyAlgorithm());
			if (_cryptographyAlgorithm == null) {
				System.err.println("Unable to load Cryptography algorithm");
				ret = false;
			}
		} 
		return ret;
	}

	private boolean initMp4Components(DecodeParameters parameters) {
		_mp4MediaReader = new MP4MediaReader();
		if (!_mp4MediaReader.loadData(parameters.getVideoPath())) {
			System.err.println("Unable to load data from orignal MP4");
			return false;
		}

		if (!_h264SteganographyContainer.loadData(_mp4MediaReader) 
				|| !_aacSteganographyContainer.loadData(_mp4MediaReader)) {
			System.err.println("Unable to load channel from original MP4");
			return false;
		}

		return true;
	}

	private boolean initSteganographyContainer(DecodeParameters parameters) {
		Preferences prefs = Preferences.getInstance();

		if (prefs.getUseVideoChannel()) {
			_h264SteganographyContainer = AlgorithmFactory.getSteganographyContainerInstanceFromName(prefs.getVideoAlgorithm());
		} else {
			_h264SteganographyContainer = AlgorithmFactory.getSteganographyContainerInstanceFromName(DEFAULT_H264_CONTAINER);
		}
		if (_h264SteganographyContainer == null) {
			System.err.println("Unable to load video steganography algorithm");
			return false;
		}

		if (prefs.getUseAudioChannel()) {
			_aacSteganographyContainer = AlgorithmFactory.getSteganographyContainerInstanceFromName(prefs.getAudioAlgorithm());
		} else {
			_aacSteganographyContainer = AlgorithmFactory.getSteganographyContainerInstanceFromName(DEFAULT_AAC_CONTAINER);
		}
		if (_aacSteganographyContainer == null) {
			System.err.println("Unable to load audio steganography algorithm");
			return false;
		}

		return true;
	}

	private boolean init(DecodeParameters parameters) {
		return initCryptographyAlgorithm() && initSteganographyContainer(parameters)
				&& initMp4Components(parameters);
	}
	
	// Process methods
	private void processContentWithCryptography(DecodeParameters parameters, byte[] content) {
		if (content == null) {
			return;
		}
		
		if (_cryptographyAlgorithm != null && parameters != null) {
			
			int padding = content.length % _cryptographyAlgorithm.getBlockSize();
			if (padding > 0) {
				content = Arrays.copyOf(content, content.length + (_cryptographyAlgorithm.getBlockSize() - padding));
			}
			
			for (int i = 0; i < content.length; i += _cryptographyAlgorithm.getBlockSize()) {
				byte[] tmp = new byte[_cryptographyAlgorithm.getBlockSize()];
				System.arraycopy(content, i, tmp, 0, _cryptographyAlgorithm.getBlockSize());
				
				if (_cryptographyAlgorithm != null && parameters != null) {
					tmp = _cryptographyAlgorithm.decrypt(tmp, parameters.getCryptographyKey().getBytes());
				} 
				
				System.arraycopy(tmp, 0, content, i, _cryptographyAlgorithm.getBlockSize());
			}
		}
	}

	private void unshuffleUnhideContent(byte audioContent[], byte videoContent[]) {
		ByteArrayOutputStream unhideDataStream = new ByteArrayOutputStream();
		int idxAudioContent = 0;
		int idxVideoContent = 0;
		boolean done = false;
		
		while (!done) {
			if (audioContent != null && idxAudioContent < audioContent.length) {
				unhideDataStream.write(audioContent[idxAudioContent]);
				idxAudioContent++;
			}
			if (videoContent != null && idxVideoContent < videoContent.length) {
				unhideDataStream.write(videoContent[idxVideoContent]);
				idxVideoContent++;
			}
			if ((audioContent == null || idxAudioContent >= audioContent.length) && 
					(videoContent == null || idxVideoContent >= videoContent.length)) {
				done = true;
			}
		}
		_unHideData = unhideDataStream.toByteArray();
	}
	
	// Finalise method
	private void finalise(DecodeParameters parameters) {

		try {
			if (parameters.getDisplay()) {
				parameters.setDisplayText(new String(_unHideData, "US-ASCII"));
				return;
			}

			String filename = parameters.getDestinationVideoDirectory() + parameters.getOutputName();
			FileOutputStream output = new FileOutputStream(filename);
			output.write(_unHideData);
			output.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
