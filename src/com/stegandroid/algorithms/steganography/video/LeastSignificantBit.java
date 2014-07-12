package com.stegandroid.algorithms.steganography.video;

import com.stegandroid.algorithms.ISteganographyAlgorithm;

public class LeastSignificantBit implements ISteganographyAlgorithm{

	@Override
	public byte[] encode(byte[] signal, byte[] content) {
		return signal.clone();
	}

	@Override
	public byte[] decode(byte[] signal) {
		return "This is a test for least significant bit algorithm".getBytes();
	}

}
