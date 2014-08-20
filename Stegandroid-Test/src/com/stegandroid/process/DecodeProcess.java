package com.stegandroid.process;

import java.io.FileOutputStream;
import java.io.IOException;

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

		if (!init(parameters))
			return false;

		if (parameters.getUseVideoChannel()) {
			_h264SteganographyContainer.unHideData();
			_unHideData = _h264SteganographyContainer.getUnHideData();
		} else if (parameters.getUseAudioChannel()) {
			_aacSteganographyContainer.unHideData();
			_unHideData = _aacSteganographyContainer.getUnHideData();
		} else {
			return false;
		}

		if (_unHideData != null) {
			_unHideData = processCryptography(parameters, _unHideData);

			this.finalise(parameters);
			return true;
		}
		return false;
	}

	private byte[] processCryptography(DecodeParameters parameters, byte[] content) {
		if (_cryptographyAlgorithm != null && parameters != null) {
			return (_cryptographyAlgorithm.encrypt(content, parameters.getCryptographyKey().getBytes()));
		}
		// Return the original content if no cryptography algorithm
		return content;
	}

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

		boolean ok = false;
		if (parameters.getUseAudioChannel())
			ok = _aacSteganographyContainer.loadData(_mp4MediaReader);
		else if (parameters.getUseVideoChannel())
			ok = _h264SteganographyContainer.loadData(_mp4MediaReader);
		
		if (!ok) {
			System.err.println("Unable to load channel from original MP4");
			return false;
		}

		return true;
	}

	private boolean initSteganographyContainer(DecodeParameters parameters) {
		//Preferences prefs = Preferences.getInstance();

		// TODO: use pref instead of parameters
		if (parameters.getUseVideoChannel()) {
			_h264SteganographyContainer = AlgorithmFactory.getSteganographyContainerInstanceFromName(parameters.getVideoAlgorithm());
		} else {
			_h264SteganographyContainer = AlgorithmFactory.getSteganographyContainerInstanceFromName(DEFAULT_H264_CONTAINER);
		}
		if (_h264SteganographyContainer == null) {
			System.err.println("Unable to load video steganography algorithm");
			return false;
		}

		if (parameters.getUseAudioChannel()) {
			_aacSteganographyContainer = AlgorithmFactory.getSteganographyContainerInstanceFromName(parameters.getAudioAlgorithm());
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
}
