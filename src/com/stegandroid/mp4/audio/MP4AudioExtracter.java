package com.stegandroid.mp4.audio;

import java.io.RandomAccessFile;
import java.util.List;

import net.sourceforge.jaad.aac.Decoder;
import net.sourceforge.jaad.aac.SampleBuffer;
import net.sourceforge.jaad.mp4.MP4Container;
import net.sourceforge.jaad.mp4.api.AudioTrack;
import net.sourceforge.jaad.mp4.api.Frame;

public class MP4AudioExtracter {

	public static byte[] getAacAudioAsByteArray(String videoPath) throws Exception {
		RandomAccessFile inputStream;
		MP4Container container; 
		List<net.sourceforge.jaad.mp4.api.Track> tracks;
		net.sourceforge.jaad.mp4.api.Track track;
		AacByteArrayWriter aacByteArrayWriter = null;
		Decoder dec;
		SampleBuffer buf;
		
		inputStream = new RandomAccessFile(videoPath, "r");
		container = new MP4Container(inputStream);
		tracks = container.getMovie().getTracks(AudioTrack.AudioCodec.AAC);
		
		if (tracks.isEmpty()) {
			throw new Exception("Track is empty... FUCK");
		}
		
		track = tracks.get(0);
		dec = new Decoder(track.getDecoderSpecificInfo());
		buf = new SampleBuffer();
		while(track.hasMoreFrames()) {
			Frame frame = track.readNextFrame();
			dec.decodeFrame(frame.getData(), buf);
 				
			if (aacByteArrayWriter == null) {
				aacByteArrayWriter = new AacByteArrayWriter(buf.getSampleRate(), buf.getChannels());
			}
			aacByteArrayWriter.write(frame.getData(), 0, frame.getData().length);
		}
		return aacByteArrayWriter.getAudioAsByteArray();
	}
	
}
