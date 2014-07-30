package com.stegandroid.algorithms.steganography.video;

import java.nio.ByteBuffer;

import com.coremedia.iso.IsoTypeReaderVariable;
import com.googlecode.mp4parser.authoring.Sample;
import com.stegandroid.utils.Utils;

public class H264SteganographyContainerLsb extends H264SteganographyContainer {

	private static final int BYTE_SIZE = 8;
	
	private int _nbBitToHideInOneByte;

	public H264SteganographyContainerLsb() {
		_nbBitToHideInOneByte = 4;
	}
	
	@Override
	public void hideData(byte[] data) {
		Sample sample;
		ByteBuffer sampleBuffer;
		ByteBuffer dataBuffer;
		int subSampleLength = 0;
		int oldSampleIdx = -1;
				
		if (_sampleList == null || _sampleListPosition >= _sampleList.size() || data == null) {
			return;
		}
		
		sample = _sampleList.get(_sampleListPosition);
		sampleBuffer = sample.asByteBuffer();
		dataBuffer = ByteBuffer.wrap(data);

		if (_subSampleIdx == 0 && _subSampleOffset == 0 && _sampleListPosition == 0) {
			addData(new byte[]{0, 0, 0, 1});
		}
		
		if (_subSampleIdx > 0) {
			for (int i = _subSampleIdx; i > 0; --i) {
				int samplelength = (int) IsoTypeReaderVariable.read(sampleBuffer, _sampleLengthSize);
				sampleBuffer.position(sampleBuffer.position() + samplelength);
            }
		}
		while (dataBuffer.remaining() > 0) {
			if (sampleBuffer.remaining() == 0) {
				_subSampleIdx = 0;
				oldSampleIdx = -1;
				_subSampleOffset = 0;
				_sampleListPosition++;
				if (_sampleListPosition >= _sampleList.size()) {
					return;
				}
				sample = _sampleList.get(_sampleListPosition);
				sampleBuffer = sample.asByteBuffer();
				addData(new byte[]{0, 0, 0, 1});
			}
			if (oldSampleIdx != _subSampleIdx) {
				subSampleLength = (int) IsoTypeReaderVariable.read(sampleBuffer, _sampleLengthSize);
			}

			// process
			oldSampleIdx = _subSampleIdx;
			applyLsb((ByteBuffer) sampleBuffer.slice().position(_subSampleOffset).limit(subSampleLength), dataBuffer);
			if (_subSampleOffset == subSampleLength) {
				sampleBuffer.position(sampleBuffer.position() + subSampleLength);
				_subSampleOffset = 0;
			}
		}
	}
	
	private void applyLsb(ByteBuffer sample, ByteBuffer data) {
		int sizeToWrite = 0;
		byte[] dataToHide = null;

		if (data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte < sample.remaining()) {
	       	_subSampleOffset += data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte; 
	       	sizeToWrite = data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte;
       		dataToHide = new byte[data.remaining()];
       		data.get(dataToHide);
	       	data.position(data.capacity());
        } else if (data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte > sample.remaining()) {
        	_subSampleOffset += sample.remaining();
        	_subSampleIdx++;
        	sizeToWrite = sample.remaining();
        } else {
        	_subSampleOffset += sample.remaining();
        	_subSampleIdx++;
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
	
}
