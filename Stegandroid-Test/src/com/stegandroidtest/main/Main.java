package com.stegandroidtest.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.H264TrackImpl;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.mp4.MP4MediaReader;
import com.stegandroid.parameters.DecodeParameters;
import com.stegandroid.parameters.EncodeParameters;
import com.stegandroid.process.DecodeProcess;
import com.stegandroid.process.EncodeProcess;

public class Main {

	public static void main(String[] args) {

		//testEncode();
		testDecode();
	}

	public static void create_mp4() {
		H264TrackImpl h264Track; 
		AACTrackImpl aacTrack; 
		Movie movie;
		Container container;
		FileOutputStream fileOutputStream;
		FileChannel fileChannel;        
		
		
		try {
			//h264Track = new H264TrackImpl(new FileDataSourceImpl("path_to_h264.h264"));

			aacTrack = new AACTrackImpl(new FileDataSourceImpl("aac-hide.aac"));

			movie = new Movie();
			//movie.addTrack(h264Track);
			movie.addTrack(aacTrack);

			container = new DefaultMp4Builder().build(movie);

			DefaultMp4Builder test = new DefaultMp4Builder();

			fileOutputStream = new FileOutputStream("my_output.mp4");
			fileChannel = fileOutputStream.getChannel();
			container.writeContainer(fileChannel);
			fileOutputStream.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testFps() {
		MP4MediaReader reader = new MP4MediaReader();
		reader.loadData("sample\\Evanescence_-_Bring_Me_To_Life.mp4");
		//		reader.loadData("sample\\20140328_001137.mp4");

		System.out.println(reader.getFramePerSeconds());
		System.out.println(reader.getTimescale() / reader.getDurationPerSample());
		reader.close();
	}

	public static void testEncode() {
		// Encode parameters
		EncodeParameters parameters = new EncodeParameters();	
		parameters.setCryptographyKey("This is a key!!!");
		parameters.setDestinationVideoDirectory("sample\\");
		parameters.setSourceVideoPath("sample\\Evanescence_-_Bring_Me_To_Life.mp4");
		//		parameters.setSourceVideoPath("sample\\20140328_001137.mp4");
		//		parameters.setSourceVideoPath("sample\\output.mp4");
		for (int i = 0; i < 50000; ++i) {
			parameters.setTextToHide(parameters.getTextToHide() + "This is a text to hide");
		}
		parameters.setHidingText(true);

		// Preferences
		Preferences.getInstance().setUseAudioChannel(false);
		Preferences.getInstance().setAudioAlgorithm("com.stegandroid.algorithms.steganography.audio.AACSteganographyContainerLsb");
		Preferences.getInstance().setUseMetadataChannel(false);
		Preferences.getInstance().setUseCryptography(false);
		Preferences.getInstance().setCryptographyAlgorithm("com.stegandroid.algorithms.cryptography.AdvancedEncryptionStandard128");
		Preferences.getInstance().setUseVideoChannel(true);
		Preferences.getInstance().setVideoAlgorithm("com.stegandroid.algorithms.steganography.video.H264SteganographyContainerLsb");

		// Encode process
		EncodeProcess process = new EncodeProcess();
		process.encode(parameters);
	}

	public static void testDecode() {
		DecodeParameters parameters = new DecodeParameters();
		parameters.setUseAudioChannel(true);
		parameters.setUseVideoChannel(false);
		parameters.setDestinationVideoDirectory("sample/");
		parameters.setDisplay(false);
		parameters.setVideoPath("my_output.mp4");

		// Preferences
		Preferences.getInstance().setUseCryptography(false);
		Preferences.getInstance().setCryptographyAlgorithm("com.stegandroid.algorithms.cryptography.AdvancedEncryptionStandard128");

		DecodeProcess process = new DecodeProcess();
		process.decode(parameters);

	}
}
