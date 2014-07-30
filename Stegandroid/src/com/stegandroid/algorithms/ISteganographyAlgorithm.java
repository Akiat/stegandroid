package com.stegandroid.algorithms;

public interface ISteganographyAlgorithm {

	public byte[] encode(byte[] signal, byte[] content);
	public byte[] decode(byte[] signal);
	
}
