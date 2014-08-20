package com.stegandroid.algorithms.steganography.audio;

import java.nio.ByteBuffer;

import com.googlecode.mp4parser.authoring.Sample;
import com.stegandroid.lsb.LSBDecode;
import com.stegandroid.tools.Utils;

public class AACSteganographyContainerLsb extends AACSteganographyContainer {

	private static final int BYTE_SIZE = 8;
	private int _nbBitToHideInOneByte;


	public AACSteganographyContainerLsb() {
		_nbBitToHideInOneByte = 2;
	}
	
	@Override
	public void unHideData() {
		LSBDecode decoder = new LSBDecode();
		for (Sample sample : _sampleList) {
			ByteBuffer buf = sample.asByteBuffer();
			buf.clear();
			byte[] bytes = new byte[buf.capacity()];
			buf.get(bytes, 0, bytes.length);
			
			_unHideData = decoder.decodeFrame(bytes);
			if (_unHideData != null){
				break;
			}
		}
	}
	
	@Override
	public void hideData(byte[] data) {
		Sample sample;
		ByteBuffer dataBuffer;
		ByteBuffer sampleBuffer;
		
		
		if (_sampleList == null || _sampleListPosition >= _sampleList.size() || data == null) {
			return;
		}
	
		sample = _sampleList.get(_sampleListPosition);
		sampleBuffer = sample.asByteBuffer();
		dataBuffer = ByteBuffer.wrap(data);

		if (_sampleOffset == 0) {
			writeHeader(sampleBuffer.capacity());
		}
		
		while (dataBuffer.remaining() > 0) {
			
			if (sampleBuffer.remaining() == 0) {
				_sampleOffset = 0;
				_sampleListPosition++;
				if (_sampleListPosition >= _sampleList.size()) {
					return;
				}
				sample = _sampleList.get(_sampleListPosition);
				sampleBuffer = sample.asByteBuffer();
				writeHeader(sampleBuffer.capacity());
			}
			applyLsb((ByteBuffer) sampleBuffer.slice().position(_sampleOffset), dataBuffer);			
			if (_sampleOffset == sampleBuffer.capacity()) {
				sampleBuffer.position(sampleBuffer.position() + _sampleOffset);
				_sampleOffset = 0;			
			}
		}
		if (sampleBuffer.remaining() == 0) {
			_sampleListPosition++;
		}
	}

	private void applyLsb(ByteBuffer sample, ByteBuffer data) {
		int sizeToWrite = 0;
		byte[] dataToHide = null;

		if (data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte < sample.remaining()) {
	       	_sampleOffset += data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte; 
	       	sizeToWrite = data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte;
       		dataToHide = new byte[data.remaining()];
       		data.get(dataToHide);
	       	data.position(data.capacity());
        } else if (data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte > sample.remaining()) {
        	_sampleOffset += sample.remaining();
        	sizeToWrite = sample.remaining();
        } else {
        	_sampleOffset += sample.remaining();
	       	sizeToWrite = data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte;
       		dataToHide = new byte[data.remaining()];
       		data.get(dataToHide);
       		data.position(data.capacity());
        }
    	// Hide data
	    byte [] tmp = new byte[sizeToWrite];
        sample.get(tmp);
        encode(tmp, dataToHide);
        this.addData(tmp);
	}
	
	private void encode(byte[] signal, byte[] content) {
		int i = 0;
		
		if (content == null || content.length == 0) {
			return;
		}

		for (int bitCount = 0; bitCount < content.length * BYTE_SIZE;) {
			for (int j = 0; (j < _nbBitToHideInOneByte) && (bitCount < content.length * BYTE_SIZE); j++) {
				int bitValue = Utils.getBitInByteArray(content, bitCount);
				signal[i] = Utils.setSpecificBit(signal[i], bitValue, j);
				bitCount++;
			}
			i++;
		}
	}

	@Override
	public byte[] getUnHideData() {
		return _unHideData;
	}
}
