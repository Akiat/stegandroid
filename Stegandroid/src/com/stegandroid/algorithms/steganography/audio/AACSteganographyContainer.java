package com.stegandroid.algorithms.steganography.audio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.coremedia.iso.boxes.mdat.SampleList;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Sample;
import com.stegandroid.algorithms.ISteganographyContainer;
import com.stegandroid.mp4.MP4MediaReader;
import com.stegandroid.mp4.StegandroidMemoryDataSourceImpl;

public class AACSteganographyContainer implements ISteganographyContainer {
	
	private final int MAX_SIZE_BUFFERING = 250000000; // 250 Mo
	
	protected OutputStream _content;
	protected SampleList _sampleList;
	protected int _sampleListPosition;
	protected int _sampleOffset;
	protected int _sampleFrequency;
	protected int _channelConfiguration;
	
	public AACSteganographyContainer() {
		_content = null;
		_sampleList = null;
		_sampleListPosition = 0;
		_sampleOffset = 0;
		_sampleFrequency = 0;
		_channelConfiguration = 0;
	}
	
	public boolean loadData(MP4MediaReader mediaReader) {
		if (mediaReader != null) {
			_content = new ByteArrayOutputStream();
			_sampleList = mediaReader.getAudioSampleList();
			_sampleListPosition = 0;
			_sampleOffset = 0;
			_sampleFrequency = mediaReader.getSamplingFrequency();
			_channelConfiguration = mediaReader.getChannelConfiguration();
			return true;
		}
		return false;
	}
	
	public DataSource getDataSource() {
		DataSource dataSource;
		
		if (_content != null && _content instanceof ByteArrayOutputStream) {
			dataSource = new StegandroidMemoryDataSourceImpl(((ByteArrayOutputStream)_content).toByteArray());
		} else {
			try {
				dataSource = new FileDataSourceImpl(new File("aac.tmp"));
			} catch (FileNotFoundException e) {
				dataSource = null;
				e.printStackTrace();
			}
		}
		return dataSource;
	}

	
	public void writeRemainingSamples() {
		Sample sample;
		ByteBuffer buffer;
		        
		if (_sampleList == null) {
			return;
		}
		
		for (; _sampleListPosition < _sampleList.size(); ++_sampleListPosition) {
			sample = _sampleList.get(_sampleListPosition);
			buffer = sample.asByteBuffer();

            if (_sampleOffset > 0) {
				buffer.position(_sampleOffset);
				_sampleOffset = 0;
            } else {
                this.writeHeader(buffer.capacity());
            }
            byte [] tmp = new byte[buffer.remaining()];
            buffer.get(tmp);
            this.addData(tmp);
		}
	}
	
	protected void writeHeader(int frameLength) {
		int profile = 2;  //AAC-LC
		byte[] header = new byte[7];
		
		// ADTS data
		header[0] = (byte) 0xFF;
		header[1] = (byte) 0xF9;
		header[2] = ((byte) (((profile - 1) << 6) + (_sampleFrequency << 2) + (_channelConfiguration >> 2)));
		header[3] = ((byte) (((_channelConfiguration & 3) << 6) + ((frameLength + 7) >> 11)));
		header[4] = ((byte) (((frameLength + 7) & 0x7FF) >> 3));
		header[5] = ((byte) ((((frameLength + 7) & 7) << 5) + 0x1F));
		header[6] = ((byte) 0x00);
		
		this.addData(header);
	}

	
	private void switchOutputStreamToFile() {
		if (_content != null && _content instanceof ByteArrayOutputStream
			&& ((ByteArrayOutputStream)_content).size() >= MAX_SIZE_BUFFERING) {
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(new File("aac.tmp"));
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
