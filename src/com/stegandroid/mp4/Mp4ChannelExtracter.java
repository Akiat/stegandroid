package com.stegandroid.mp4;

import java.io.IOException;

import com.stegandroid.mp4.audio.MP4AudioExtracter;
import com.stegandroid.mp4.video.MP4VideoExtracter;

public class Mp4ChannelExtracter {
	
	
	public Mp4ChannelExtracter() {
		
	}

	public byte[] extractH264(String videoPath) {
		try {
			return MP4VideoExtracter.getH264VideoAsByteArray(videoPath);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
	
	public byte[] extractAAC(String videoPath) {
		try {
			return MP4AudioExtracter.getAacAudioAsByteArray(videoPath);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	public byte[] extractMetadata(String videoPath) {
		return null;
	}
	

	
}
