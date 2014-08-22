package com.stegandroid.process;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

import com.stegandroid.algorithms.AlgorithmFactory;
import com.stegandroid.algorithms.ICryptographyAlgorithm;
import com.stegandroid.algorithms.ISteganographyContainer;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.lsb.LSBEncode;
import com.stegandroid.mp4.MP4MediaReader;
import com.stegandroid.mp4.MP4MediaWriter;
import com.stegandroid.parameters.EncodeParameters;


public class EncodeProcess {

	private final int DEFAULT_BLOCK_SIZE = 16;
	private final String DEFAULT_H264_CONTAINER = "com.stegandroid.algorithms.steganography.video.H264SteganographyContainer";
	private final String DEFAULT_AAC_CONTAINER = "com.stegandroid.algorithms.steganography.audio.AACSteganographyContainer";
	
	private MP4MediaReader _mp4MediaReader;
	private ISteganographyContainer _h264SteganographyContainer;
	private ISteganographyContainer _aacSteganographyContainer;	
	private ICryptographyAlgorithm _cryptographyAlgorithm;
	
	private InputStream _contentToHideStream;
	private byte[]		_encryptedBytes;
	private int _blockSize;
	
	public EncodeProcess() {
		_mp4MediaReader = null;
		_h264SteganographyContainer = null;
		_aacSteganographyContainer = null;
		_cryptographyAlgorithm = null;
		_contentToHideStream = null;
		_blockSize = DEFAULT_BLOCK_SIZE;
		_encryptedBytes = null;
	}

	private boolean init(EncodeParameters parameters) {
		return initCryptographyAlgorithm() && initContentToHideStream(parameters)
				&& initSteganographyContainer() && initMp4Components(parameters);
	}
	
	private boolean initCryptographyAlgorithm() {
		Preferences pref = Preferences.getInstance();
		boolean ret = true;
		
		if (pref.getUseCryptography()) {
			_cryptographyAlgorithm = AlgorithmFactory.getCryptographyAlgorithmInstanceFromName(pref.getCryptographyAlgorithm());
			if (_cryptographyAlgorithm == null) {
				System.err.println("Unable to load Cryptography algorithm");
				ret = false;
			} else {
				_blockSize = _cryptographyAlgorithm.getBlockSize();
			}
		} 
		return ret;
	}
	
	private boolean initContentToHideStream(EncodeParameters parameters) {
		boolean ret = true;
		
		if (parameters.isHidingText()) {
			_contentToHideStream = new ByteArrayInputStream(parameters.getTextToHide().getBytes());
		} else {
			try {
				_contentToHideStream = new FileInputStream(parameters.getFileToHidePath());
			} catch (FileNotFoundException e) {
				_contentToHideStream = null;
				System.err.println("Unable to load content to hide");
				ret = false;
			}
		}
		return ret;
	}
	
	private boolean initSteganographyContainer() {
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
	
	private boolean initMp4Components(EncodeParameters parameters) {
		_mp4MediaReader = new MP4MediaReader();
		if (!_mp4MediaReader.loadData(parameters.getSourceVideoPath())) {
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
	
	public boolean encode(EncodeParameters parameters) {
		
		Preferences prefs = Preferences.getInstance();
		
		if (!this.init(parameters)) {
			return false;
		}
	
		try {
			byte[] bytes = IOUtils.toByteArray(_contentToHideStream);

			if (prefs.getUseCryptography())
			{
				int padding = bytes.length % 16;
				if (padding > 0)
					bytes = Arrays.copyOf(bytes, bytes.length + (16 - padding));
				
				_encryptedBytes = new byte[bytes.length];
				for (int i = 0; i < bytes.length; i += 16) {
					byte[] tmp = new byte[16];
					System.arraycopy(bytes, i, tmp, 0, 16);
					
					tmp = processCryptography(parameters, tmp);
					
					System.arraycopy(tmp, 0, _encryptedBytes, i, 16);
				}
			} else
				_encryptedBytes = bytes;

			
			if (prefs.getUseVideoChannel()) {
				_h264SteganographyContainer.hideData(_encryptedBytes);
			} else if (prefs.getUseAudioChannel()) {
				_aacSteganographyContainer.hideData(_encryptedBytes);
			}
			
			finalise(parameters);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	private byte[] processCryptography(EncodeParameters parameters, byte[] content) {		
		if (_cryptographyAlgorithm != null && parameters != null) {
			return (_cryptographyAlgorithm.encrypt(content, parameters.getCryptographyKey().getBytes()));
		}
		// Return the original content if no cryptography algorithm
		return content;
	}

	private void finalise(EncodeParameters parameters) {
		MP4MediaWriter mp4MediaWriter;

		if (_h264SteganographyContainer != null) {
			_h264SteganographyContainer.writeRemainingSamples();
		}
		if (_aacSteganographyContainer != null) {
			_aacSteganographyContainer.writeRemainingSamples();
		}
		mp4MediaWriter = new MP4MediaWriter(parameters.getDestinationVideoDirectory() + "output.mp4", 
				_mp4MediaReader.getTimescale(), (int) _mp4MediaReader.getDurationPerSample(),
				_h264SteganographyContainer.getDataSource(), _aacSteganographyContainer.getDataSource());
		mp4MediaWriter.create();
	}
}
