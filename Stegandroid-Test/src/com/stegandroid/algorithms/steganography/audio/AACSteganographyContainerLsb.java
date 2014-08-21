package com.stegandroid.algorithms.steganography.audio;

import java.nio.ByteBuffer;

import com.googlecode.mp4parser.authoring.Sample;
import com.stegandroid.lsb.LSBDecode;
import com.stegandroid.lsb.LSBEncode;

public class AACSteganographyContainerLsb extends AACSteganographyContainer {

	private static final int BYTE_SIZE = 8;
	private int _nbBitToHideInOneByte;
	private int _dataToHideBitOffset;

	public AACSteganographyContainerLsb() {
		_nbBitToHideInOneByte = 1;
		_dataToHideBitOffset = 0;
	}
	
	@Override
	public void unHideData() {
		LSBDecode decoder = new LSBDecode();
		for (Sample sample : _sampleList) {
			byte[] frame = sampleToByteArray(sample);
			
			_unHideData = decoder.decodeFrame(frame);
			if (_unHideData != null){
				break;
			}
		}
	}
	
	@Override
	public void hideData(byte[] toHide) {
		
		if (_sampleList == null || toHide == null) {
			return;
		}
	
		// TODO : nbBitinOneByte a changer par les preferences
		LSBEncode encoder = new LSBEncode(toHide, 1);
		for (Sample sample : _sampleList) {
			byte[] frame = sampleToByteArray(sample);

			frame = encoder.encodeNextFrame(frame);
			writeHeader(frame.length);
			this.addData(frame);
			
			_sampleListPosition++;
		}
		
	}
	
	private byte[] sampleToByteArray(Sample sample) {
		ByteBuffer buf = sample.asByteBuffer();
		byte[] frame = new byte[buf.capacity()];
		buf.get(frame);
		
		return frame;
	}

	@Override
	public byte[] getUnHideData() {
		return _unHideData;
	}
}
