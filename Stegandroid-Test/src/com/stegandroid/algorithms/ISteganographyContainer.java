package com.stegandroid.algorithms;

import com.googlecode.mp4parser.DataSource;
import com.stegandroid.mp4.MP4MediaReader;

public interface ISteganographyContainer {

	public void writeRemainingSamples();
	public void hideData(byte[] content);
	public void unHideData();
	public boolean loadData(MP4MediaReader mediaReader);
	public DataSource getDataSource();
	public byte[] getUnHideData();
	
}
