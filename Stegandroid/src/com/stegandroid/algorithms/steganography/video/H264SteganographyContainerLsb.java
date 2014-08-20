package com.stegandroid.algorithms.steganography.video;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.coremedia.iso.IsoTypeReaderVariable;
import com.googlecode.mp4parser.authoring.Sample;
import com.stegandroid.h264.Pair;
import com.stegandroid.tools.Utils;

public class H264SteganographyContainerLsb extends H264SteganographyContainer {

	private static final int BYTE_SIZE = 8;
	
	private int _nbBitToHideInOneByte;

	public H264SteganographyContainerLsb() {
		_nbBitToHideInOneByte = 1;
	}

	private List<Pair<ByteBuffer, Boolean>> splitSampleWithMacroblocksDataOffset(ByteBuffer buffer) {
		List<Pair<Integer, Integer>> macroblocksDataOffset;
		List<Pair<ByteBuffer, Boolean>> res = new ArrayList<Pair<ByteBuffer,Boolean>>();
		int previousOffset = 0;
		int originalSize;
		int beginOffset;
		boolean first = true;
		
		// true if we can hide data on partition
		originalSize = buffer.remaining();
//		System.out.println("Original size: " + originalSize);
		macroblocksDataOffset = getMacroblockDataOffsets(buffer.slice());
		if (macroblocksDataOffset == null) {
			res.add(new Pair<ByteBuffer, Boolean>(buffer, false));
		} else {
			for (int i = 0; i < macroblocksDataOffset.size(); ++i) {
				if (_subSampleOffset <= macroblocksDataOffset.get(i).getFirst()) {
					buffer.position(previousOffset);
					res.add(new Pair<ByteBuffer, Boolean>((ByteBuffer) buffer.slice().limit(macroblocksDataOffset.get(i).getFirst() - previousOffset), false));
//					System.out.println("Adding to direct write from " + previousOffset + " to " + (macroblocksDataOffset.get(i).getFirst() - previousOffset));
				}
				if (_subSampleOffset <= macroblocksDataOffset.get(i).getSecond()) {
					
					if (_subSampleOffset > macroblocksDataOffset.get(i).getFirst()) {
						beginOffset = _subSampleOffset;
					} else {
						beginOffset = macroblocksDataOffset.get(i).getFirst();
					}
					buffer.position(beginOffset);
					res.add(new Pair<ByteBuffer, Boolean>((ByteBuffer) buffer.slice().limit(macroblocksDataOffset.get(i).getSecond() - beginOffset), true));
//					System.out.println("Adding to lsb write from " + macroblocksDataOffset.get(i).getFirst() + " to " + (macroblocksDataOffset.get(i).getSecond() - macroblocksDataOffset.get(i).getFirst()));
				}
				previousOffset = macroblocksDataOffset.get(i).getSecond();
			}
			if (originalSize != previousOffset) {
				System.out.println("Completing...");
				buffer.position(previousOffset);
				res.add(new Pair<ByteBuffer, Boolean>((ByteBuffer) buffer.slice().limit(originalSize - previousOffset), false));
			}
		}
		return res;
	}
	
	@Override
	public void hideData(byte[] data) {
		Sample currentSample;
		ByteBuffer currentSampleBuffer;
		ByteBuffer dataBuffer;
		int currentSampleLength = -1;
		List<Pair<ByteBuffer, Boolean>> splittedSample = null;
		
		if (_sampleList == null || _sampleListPosition >= _sampleList.size() || data == null) {
			return;
		}
		
		currentSample = _sampleList.get(_sampleListPosition);
		currentSampleBuffer = currentSample.asByteBuffer();
		dataBuffer = ByteBuffer.wrap(data);
		
		if (_subSampleIdx > 0) {
			// We move the buffer to the position of the current sub index
			for (int i = 0; i < _subSampleIdx; ++i) {
				currentSampleLength = (int) IsoTypeReaderVariable.read(currentSampleBuffer, _sampleLengthSize);
				currentSampleBuffer.position(currentSampleBuffer.position() + currentSampleLength);
			}			
		}
		
		while (dataBuffer.remaining() > 0 && _sampleListPosition < _sampleList.size()) {

			if (!currentSampleBuffer.hasRemaining()) {
				// We reset the parameter and change the frame if there is no data to read in the current sample buffer
				_subSampleIdx = 0;
				_subSampleOffset = 0;
				_sampleListPosition++;
				currentSampleLength = -1;
				if (_sampleListPosition < _sampleList.size()) {
					currentSample = _sampleList.get(_sampleListPosition);
					currentSampleBuffer = currentSample.asByteBuffer().slice();
				}
				continue;
			} 

			
			if (currentSampleLength == -1) {
				currentSampleLength = (int) IsoTypeReaderVariable.read(currentSampleBuffer, _sampleLengthSize);
				splittedSample = splitSampleWithMacroblocksDataOffset((ByteBuffer) currentSampleBuffer.slice().limit(currentSampleLength));
				if (_subSampleOffset == 0) {
					this.addData(new byte[]{0, 0, 1});
				}
			}
			
			for (int i = 0; i < splittedSample.size() && dataBuffer.hasRemaining(); ++i) {
				if (splittedSample.get(i).getSecond()) {
					applyLsb(splittedSample.get(i).getFirst(), dataBuffer);
					if (splittedSample.get(i).getFirst().hasRemaining()) {
						--i;
					}
				} else {
					_subSampleOffset += splittedSample.get(i).getFirst().remaining();
					this.addData(splittedSample.get(i).getFirst());
				}
				currentSampleBuffer.position(_subSampleOffset + _sampleLengthSize);
			}
			if (_subSampleOffset >= currentSampleLength) {
				_subSampleIdx++;
			}
			currentSampleLength = -1;

		}			
	}
	
//	@Override
//	public void hideData(byte[] data) {
//		Sample currentSample;
//		ByteBuffer currentSampleBuffer;
//		ByteBuffer dataBuffer;
//		int currentSampleLength = -1;
//		int oldSampleIdx = -1;
//		int offset = -1;
//		byte tmp[];
//		
//		if (_sampleList == null || _sampleListPosition >= _sampleList.size() || data == null) {
//			return;
//		}
//		currentSample = _sampleList.get(_sampleListPosition);
//		currentSampleBuffer = currentSample.asByteBuffer().slice();
//		dataBuffer = ByteBuffer.wrap(data);
//		
//		if (_subSampleIdx > 0) {
//			// We move the buffer to the position of the current sub index
//			for (int i = 0; i < _subSampleIdx; ++i) {
//				currentSampleLength = (int) IsoTypeReaderVariable.read(currentSampleBuffer, _sampleLengthSize);
//				currentSampleBuffer.position(currentSampleBuffer.position() + currentSampleLength);
//			}
//		}
//		
//		while (dataBuffer.remaining() > 0 && _sampleListPosition < _sampleList.size()) {			
//			
//			if (!currentSampleBuffer.hasRemaining()) {
//				// We reset the parameter and change the frame if there is no data to read in the current sample buffer
//				_subSampleIdx = 0;
//				_subSampleOffset = 0;
//				_sampleListPosition++;
//				oldSampleIdx = -1;
//				if (_sampleListPosition < _sampleList.size()) {
//					currentSample = _sampleList.get(_sampleListPosition);
//					currentSampleBuffer = currentSample.asByteBuffer().slice();
//				}
//				continue;
//			} 
//
//			if (_subSampleIdx != oldSampleIdx) {
//				currentSampleLength = -1;
//			}
//			
//			if (currentSampleLength == -1) {
//				currentSampleLength = (int) IsoTypeReaderVariable.read(currentSampleBuffer, _sampleLengthSize);
//				// If this is the beginning of the frame, we write the syncrhonisation sequence and NALU
//				if (_subSampleOffset == 0) {
//					addData(new byte[]{0, 0, 0, 1});
//					offset = getSliceDataOffset((ByteBuffer) currentSampleBuffer.slice().limit(currentSampleLength));
//					if (offset == -1) {
//						tmp = new byte[currentSampleLength];
//					} else {
//						tmp = new byte[offset];
//					}
//					currentSampleBuffer.get(tmp);
//					this.addData(tmp);
//					_subSampleOffset = currentSampleBuffer.position() - _sampleLengthSize;
//				} else {
//					currentSampleBuffer.position(currentSampleBuffer.position() + _subSampleOffset);
//				}
//			}
//			
//			// We apply the LSB transformation
//			oldSampleIdx = _subSampleIdx;
////			System.out.println("Current: " + currentSampleBuffer.position() + " Offset: " + _subSampleOffset);
//			applyLsb((ByteBuffer)currentSampleBuffer.slice().limit(currentSampleLength - _subSampleOffset), dataBuffer);
//
//			if (_subSampleIdx == oldSampleIdx) {
//				currentSampleBuffer.position(_subSampleOffset);
//			} else {
//				currentSampleBuffer.position(currentSampleLength + _sampleLengthSize);
//			}
//		}
//	}
	
	
	
	private void applyLsb(ByteBuffer sample, ByteBuffer data) {
		int requiredSize;
		int sizeToWrite;
		boolean encode = true;
		byte[] sampleArray;
		byte[] dataArray;
		
		requiredSize = data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte;
		if (requiredSize < sample.remaining()) {
			sizeToWrite = requiredSize;
		} else if (requiredSize == sample.remaining()) {
			sizeToWrite = sample.remaining();
		} else {
			sizeToWrite = sample.remaining();
			encode = false;
		}
		_subSampleOffset += sizeToWrite;
		
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
