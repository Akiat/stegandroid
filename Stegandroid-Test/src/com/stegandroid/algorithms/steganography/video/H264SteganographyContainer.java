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
		Sample sample;
		ByteBuffer buffer;
        ByteBuffer byteBuffer;
		
		if (_sampleList == null) {
			return;
		}
		
		for (; _sampleListPosition < _sampleList.size(); ++_sampleListPosition) {
			sample = _sampleList.get(_sampleListPosition);
			buffer = sample.asByteBuffer();
			if (_subSampleIdx > 0) {
				for (int i = _subSampleIdx; i > 0; --i) {
					int samplelength = (int) IsoTypeReaderVariable.read(buffer, _sampleLengthSize);
					buffer.position(buffer.position() + samplelength);
	            }
			}
			while (buffer.remaining() > 0) {
                int readlength = (int) IsoTypeReaderVariable.read(buffer, _sampleLengthSize);
                if (_subSampleOffset > 0) {
                    byteBuffer = (ByteBuffer) buffer.slice().position(_subSampleOffset).limit(readlength);	
                    _subSampleOffset = 0;
                } else {
                    byteBuffer = (ByteBuffer) buffer.slice().limit(readlength);	
                    this.addData(new byte[]{0, 0, 0, 1});
                }
                byte [] tmp = new byte[byteBuffer.remaining()];
                byteBuffer.get(tmp);
                this.addData(tmp);
                buffer.position(buffer.position() + readlength);
            }
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
		if (_content != null && _content instanceof ByteArrayOutputStream
				&& ((ByteArrayOutputStream)_content).size() >= MAX_SIZE_BUFFERING) {
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
