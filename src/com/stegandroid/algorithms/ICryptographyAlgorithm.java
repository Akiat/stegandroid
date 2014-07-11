package com.stegandroid.algorithms;

public interface ICryptographyAlgorithm {

	public byte[] encrypt(byte[] message, byte[] key);
	public byte[] decrypt(byte[] cipher, byte[] key);
	
}
