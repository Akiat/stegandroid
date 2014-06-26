package com.stegandroid.algorithms;

public interface IAlgorithm {

	public byte[] encode(byte[] signal, byte[] content);
	public byte[] decode(byte[] signal);
	
}
