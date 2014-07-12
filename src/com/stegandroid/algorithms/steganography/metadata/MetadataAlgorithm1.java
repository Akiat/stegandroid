package com.stegandroid.algorithms.steganography.metadata;

import com.stegandroid.algorithms.ISteganographyAlgorithm;

public class MetadataAlgorithm1 implements ISteganographyAlgorithm{

	@Override
	public byte[] encode(byte[] signal, byte[] content) {
		return signal.clone();
	}

	@Override
	public byte[] decode(byte[] signal) {
		return "This is a test for metadata algorithm 1".getBytes();
	}

}
