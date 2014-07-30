package com.stegandroid.mp4.audio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.util.SparseIntArray;

public class AacByteArrayWriter {

	private final ByteArrayOutputStream out;
	private final int _sampleRate;
	private final int _channels;

	public AacByteArrayWriter(int sampleRate, int channels) throws IOException {
		this._sampleRate = sampleRate;
		this._channels = channels;
		
		out = new ByteArrayOutputStream();
	}

	public void write(byte[] data) throws IOException {
		write(data, 0, data.length);
	}

	public void write(byte[] data, int off, int len) throws IOException {
		int profile = 2;  //AAC-LC
		int frequency = getSampleFrequencyCode(this._sampleRate);
		int channel = getChannelCode(this._channels);
				
		// ADTS data
		out.write((byte) 0xFF);
		out.write((byte) 0xF1);
		out.write((byte) (((profile - 1) << 6) + (frequency << 2) + (channel >> 2)));
		out.write((byte) (((channel & 3) << 6) + ((data.length + 7) >> 11)));
		out.write((byte) (((data.length + 7) & 0x7FF) >> 3));
		out.write((byte) ((((data.length + 7) & 7) << 5) + 0x1F));
		out.write((byte) 0x00);
		
		out.write(data, off, len);
	}

	private Integer getSampleFrequencyCode(Integer frequency) {
		SparseIntArray map = new SparseIntArray();

		map.put(96000, 0);
		map.put(88200, 1);
		map.put(64000, 2);
		map.put(48000, 3);
		map.put(44100, 4);
		map.put(32000, 5);
		map.put(24000, 6);
		map.put(22050, 7);
		map.put(16000, 8);
		map.put(12000, 9);
		map.put(11025, 10);
		map.put(8000, 11);
		map.put(7350, 12);
		return map.get(frequency, 3);
	}
	
	private int getChannelCode(int channels) {
		int code = 2;
		
		if (channels == 8) {
			code = 7;
		} else if (channels < 8) {
			code = channels;
		}
		return code;
	}
	
	public byte[] getAudioAsByteArray() throws IOException {
		System.out.println(out.toByteArray().length);
		out.close();
		return out.toByteArray();
	}	
}
