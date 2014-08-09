package com.stegandroid.algorithms.steganography.video;

import java.nio.ByteBuffer;

import com.coremedia.iso.IsoTypeReaderVariable;
import com.googlecode.mp4parser.authoring.Sample;
import com.stegandroid.utils.Utils;

public class H264SteganographyContainerLsb extends H264SteganographyContainer {

	private static final int BYTE_SIZE = 8;
	
	private int _nbBitToHideInOneByte;

	public H264SteganographyContainerLsb() {
		_nbBitToHideInOneByte = 2;
	}

	@Override
	public void hideData(byte[] data) {
		Sample currentSample;
		ByteBuffer currentSampleBuffer;
		ByteBuffer dataBuffer;
		int currentSampleLength = -1;
		int oldSampleIdx = -1;
				
		if (_sampleList == null || _sampleListPosition >= _sampleList.size() || data == null) {
			return;
		}
		currentSample = _sampleList.get(_sampleListPosition);
		currentSampleBuffer = currentSample.asByteBuffer().slice();
		dataBuffer = ByteBuffer.wrap(data);
		
		if (_subSampleIdx > 0) {
			// We move the buffer to the position of the current sub index
			for (int i = 0; i < _subSampleIdx; ++i) {
				currentSampleLength = (int) IsoTypeReaderVariable.read(currentSampleBuffer, _sampleLengthSize);
				currentSampleBuffer.position(currentSampleBuffer.position() + currentSampleLength);
				currentSampleLength = -1;
			}
		}
		
		while (dataBuffer.remaining() > 0 && _sampleListPosition < _sampleList.size()) {			
			
			if (!currentSampleBuffer.hasRemaining()) {
				// We reset the parameter and change the frame if there is no data to read in the current sample buffer
				_subSampleIdx = 0;
				_subSampleOffset = 0;
				_sampleListPosition++;
				if (_sampleListPosition < _sampleList.size()) {
					currentSample = _sampleList.get(_sampleListPosition);
					currentSampleBuffer = currentSample.asByteBuffer().slice();
					oldSampleIdx = -1;
				}
				continue;
			} 

			if (_subSampleIdx != oldSampleIdx) {
				currentSampleLength = -1;
				System.out.println(_subSampleIdx);
			}
			
			if (currentSampleLength == -1) {
				currentSampleLength = (int) IsoTypeReaderVariable.read(currentSampleBuffer, _sampleLengthSize);
				// If this is the beginning of the frame, we write the syncrhonisation sequence and NALU
				if (_subSampleOffset == 0) {
					addData(new byte[]{0, 0, 0, 1});
				} else {
					currentSampleBuffer.position(_subSampleOffset + _sampleLengthSize);
				}
			}
			
			// We apply the LSB transformation
			oldSampleIdx = _subSampleIdx;
			applyLsb((ByteBuffer)currentSampleBuffer.slice().limit(currentSampleLength - _subSampleOffset), dataBuffer);
			currentSampleBuffer.position(_subSampleOffset + _sampleLengthSize);
		}
	}
		
	
	private void applyLsb(ByteBuffer sample, ByteBuffer data) {
		int requiredSize;
		int sizeToWrite;
		boolean encode = true;
		byte[] sampleArray;
		byte[] dataArray;
		
		requiredSize = data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte;
		if (requiredSize < sample.remaining()) {
			_subSampleOffset += requiredSize;
			sizeToWrite = requiredSize;
//			System.out.println("Hiding content lower ");
		} else if (requiredSize == sample.remaining()) {
			_subSampleOffset += requiredSize;
			_subSampleIdx++;
			sizeToWrite = requiredSize;
//			System.out.println("Hiding content equal ");
		} else {
			_subSampleOffset += sample.remaining();
			_subSampleIdx++;
			sizeToWrite = sample.capacity();
			encode = false;
//			System.out.println("NOT hiding content");
		}
	
		sampleArray = new byte[sizeToWrite];
		sample.get(sampleArray);

		/* If there is not enough space for data, 
		** we just write the rest of the frame without encoding data to hide
		*/ 
		if (encode) {
			dataArray = new byte[data.remaining()];
			data.get(dataArray);
			encode(sampleArray, dataArray);			
		}
		
		this.addData(sampleArray);
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
