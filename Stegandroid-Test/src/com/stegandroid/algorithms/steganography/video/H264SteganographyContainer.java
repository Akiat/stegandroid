package com.stegandroid.algorithms.steganography.video;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.coremedia.iso.IsoTypeReaderVariable;
import com.coremedia.iso.boxes.mdat.SampleList;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Sample;
import com.stegandroid.algorithms.ISteganographyContainer;
import com.stegandroid.mp4.MP4MediaReader;
import com.stegandroid.mp4.StegandroidMemoryDataSourceImpl;

public class H264SteganographyContainer implements ISteganographyContainer {

	private final int MAX_SIZE_BUFFERING = 250000000; // 250 Mo
	
	protected OutputStream _content;
	protected SampleList _sampleList;
	protected int _sampleLengthSize;
	protected int _sampleListPosition;
	protected int _subSampleIdx;
	protected int _subSampleOffset;
	
	public H264SteganographyContainer() {
		_content = null;
		_sampleList = null;
		_sampleLengthSize = 0;
		_sampleListPosition = 0;
		_subSampleIdx = 0;
		_subSampleOffset = 0;
	}
	
	@Override
	public boolean loadData(MP4MediaReader mediaReader) {
		if (mediaReader != null) {
			_content = new ByteArrayOutputStream();
			_sampleList = mediaReader.getVideoSampleList();
			_sampleLengthSize = mediaReader.getVideoSampleLengthSize() + 1;
			_sampleListPosition = 0;
			_subSampleIdx = 0;
			_subSampleOffset = 0;
			this.addData(new byte[]{0, 0, 0, 1});
			this.addData(mediaReader.getSequenceParameterSets());
			this.addData(new byte[]{0, 0, 0, 1});
			this.addData(mediaReader.getPictureParameterSets());
			return true;
		}
		return false;
	}
	
	public DataSource getDataSource() {
		DataSource dataSource = null;
		
		if (_content != null) {
			if (_content instanceof ByteArrayOutputStream) {
				dataSource = new StegandroidMemoryDataSourceImpl(((ByteArrayOutputStream)_content).toByteArray());
			} else {
				try {
					_content.close();
					dataSource = new FileDataSourceImpl(new File("h264.tmp"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataSource;
	}
	
	protected void writeHeader() {
		
		this.addData(null);
	}
	
	public void writeRemainingSamples() {
		Sample currentSample;
		ByteBuffer currentSampleBuffer;
		int currentSampleLength = -1;
		byte[] dataToWrite;
		
		if (_sampleList == null || _sampleListPosition >= _sampleList.size()) {
			return;
		}
		currentSample = _sampleList.get(_sampleListPosition);
		currentSampleBuffer = currentSample.asByteBuffer().slice();
		
		if (_subSampleIdx > 0) {
			// We move the buffer to the position of the current sub index
			for (int i = 0; i < _subSampleIdx; ++i) {
				currentSampleLength = (int) IsoTypeReaderVariable.read(currentSampleBuffer, _sampleLengthSize);
				currentSampleBuffer.position(currentSampleBuffer.position() + currentSampleLength);
				currentSampleLength = -1;
			}
		}
		
		while (_sampleListPosition < _sampleList.size()) {			
			
			if (!currentSampleBuffer.hasRemaining()) {
				// We reset the parameter and change the frame if there is no data to read in the current sample buffer
				_subSampleIdx = 0;
				_subSampleOffset = 0;
				_sampleListPosition++;
				if (_sampleListPosition < _sampleList.size()) {
					currentSample = _sampleList.get(_sampleListPosition);
					currentSampleBuffer = currentSample.asByteBuffer().slice();
				}
				continue;
			} 

			currentSampleLength = (int) IsoTypeReaderVariable.read(currentSampleBuffer, _sampleLengthSize);

			// If this is the beginning of the frame, we write the syncrhonisation sequence and NALU
			if (_subSampleOffset == 0) {
				addData(new byte[]{0, 0, 0, 1});
			} else {
				currentSampleBuffer.position(_subSampleOffset + _sampleLengthSize);
			}
			
			// We write the data
			dataToWrite = new byte[currentSampleBuffer.remaining()];
			currentSampleBuffer.get(dataToWrite);
			this.addData(dataToWrite);
			currentSampleBuffer.position(currentSampleBuffer.capacity());
		}
	}
		
	public void cleanUp() {
		File f;
		
		if (_content != null && _content instanceof FileOutputStream) {
			try {
				_content.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			f = new File ("h264.tmp");
			f.delete();
		}
	}
	
	private void switchOutputStreamToFile() {
		if (_content != null && _content instanceof ByteArrayOutputStream) {
//				&& ((ByteArrayOutputStream)_content).size() >= MAX_SIZE_BUFFERING) {
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(new File("h264.tmp"));
					((ByteArrayOutputStream)_content).writeTo(fos);
					_content = fos;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
			}
		}		
	}
	
	protected void addData(byte[] content) {
		switchOutputStreamToFile();
		if (_content != null) {
			try {
//				System.out.println(content.length);
				_content.write(content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void hideData(byte[] content) {		
	}
	
}
