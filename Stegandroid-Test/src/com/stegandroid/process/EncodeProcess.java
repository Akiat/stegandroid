package com.stegandroid.process;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.stegandroid.algorithms.AlgorithmFactory;
import com.stegandroid.algorithms.ICryptographyAlgorithm;
import com.stegandroid.algorithms.ISteganographyContainer;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.mp4.MP4MediaReader;
import com.stegandroid.mp4.MP4MediaWriter;
import com.stegandroid.parameters.EncodeParameters;


public class EncodeProcess {

	private final int DEFAULT_BLOCK_SIZE = 1024;
	private final String DEFAULT_H264_CONTAINER = "com.stegandroid.algorithms.steganography.video.H264SteganographyContainer";
	private final String DEFAULT_AAC_CONTAINER = "com.stegandroid.algorithms.steganography.audio.AACSteganographyContainer";
	
	private MP4MediaReader _mp4MediaReader;
	private ISteganographyContainer _h264SteganographyContainer;
	private ISteganographyContainer _aacSteganographyContainer;	
	private ICryptographyAlgorithm _cryptographyAlgorithm;
	
	private InputStream _contentToHideStream;
	private int _blockSize;
	
	public EncodeProcess() {
		_mp4MediaReader = null;
		_h264SteganographyContainer = null;
		_aacSteganographyContainer = null;
		_cryptographyAlgorithm = null;
		_contentToHideStream = null;
		_blockSize = DEFAULT_BLOCK_SIZE;
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
			return false;
		}

		if (prefs.getUseAudioChannel()) {
			_aacSteganographyContainer = AlgorithmFactory.getSteganographyContainerInstanceFromName(prefs.getAudioAlgorithm());
		} else {
			_aacSteganographyContainer = AlgorithmFactory.getSteganographyContainerInstanceFromName(DEFAULT_AAC_CONTAINER);
		}
		if (_aacSteganographyContainer == null) {
			return false;
		}
		
		return true;
	}
	
	private boolean initMp4Components(EncodeParameters parameters) {
		_mp4MediaReader = new MP4MediaReader();
		if (!_mp4MediaReader.loadData(parameters.getSourceVideoPath())) {
			return false;
		}
		
		if (!_h264SteganographyContainer.loadData(_mp4MediaReader) 
				|| !_aacSteganographyContainer.loadData(_mp4MediaReader)) {
			return false;
		}
		
		return true;
	}
	
	public boolean encode(EncodeParameters parameters) {
		byte[] data = new byte[_blockSize];
		int readedContent = 0;
		
		if (!this.init(parameters)) {
			return false;
		}
		try {
			// TODO: Replace this horrible fucking crap
			processVideo(ByteBuffer.allocate(4).putInt(_contentToHideStream.available()).array());
			
			while ((readedContent += _contentToHideStream.read(data, readedContent, _blockSize - readedContent)) != -1) {
				if (readedContent < _blockSize) {
					continue;
				}
				data = processCryptography(parameters, data);
				// video
				//processVideo(data);
				// audio
				processAudio(data);
				// metadata
				readedContent = 0;
				data = new byte[_blockSize];
			}
			finalise(parameters);
			_contentToHideStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private byte[] processCryptography(EncodeParameters parameters, byte[] content) {
		byte[] ret = null;
		
		if (_cryptographyAlgorithm != null && parameters != null) {
			ret = _cryptographyAlgorithm.encrypt(content, parameters.getCryptographyKey().getBytes());
		}
		return ret;
	}
	
	private void processVideo(byte[] content) {
		if (_h264SteganographyContainer != null) {
			_h264SteganographyContainer.hideData(content);
		}
	}
	
	private void processAudio(byte[] content) {
		if (_aacSteganographyContainer != null) {
			_aacSteganographyContainer.hideData(content);
		} 
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
