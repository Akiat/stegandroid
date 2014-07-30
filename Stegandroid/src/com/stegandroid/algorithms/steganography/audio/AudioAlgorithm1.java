package com.stegandroid.algorithms.steganography.audio;

import com.stegandroid.algorithms.ISteganographyAlgorithm;

public class AudioAlgorithm1 implements ISteganographyAlgorithm{

	@Override
	public byte[] encode(byte[] signal, byte[] content) {
		return signal.clone();
	}

	@Override
	public byte[] decode(byte[] signal) {
		return "This is a test for audio algorithm 1".getBytes();
	}

}
