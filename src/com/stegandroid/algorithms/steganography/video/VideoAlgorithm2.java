package com.stegandroid.algorithms.steganography.video;

import com.stegandroid.algorithms.ISteganographyAlgorithm;

public class VideoAlgorithm2 implements ISteganographyAlgorithm{

	@Override
	public byte[] encode(byte[] signal, byte[] content) {
		return signal.clone();
	}

	@Override
	public byte[] decode(byte[] signal) {
		return "This is a test for video algorithm 2".getBytes();
	}

}
