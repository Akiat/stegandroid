package com.stegandroid.algorithms.steganography.metadata;

public class MetadataAlgorithm2{

	public byte[] encode(byte[] signal, byte[] content) {
		return signal.clone();
	}

	public byte[] decode(byte[] signal) {
		return "This is a test for metadata algorithm 2".getBytes();
	}

}
