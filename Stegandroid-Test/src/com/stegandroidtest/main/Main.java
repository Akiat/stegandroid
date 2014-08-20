package com.stegandroidtest.main;

import com.stegandroid.configuration.Preferences;
import com.stegandroid.mp4.MP4MediaReader;
import com.stegandroid.parameters.EncodeParameters;
import com.stegandroid.process.DecodeProcess;
import com.stegandroid.process.EncodeProcess;

public class Main {

	public static void main(String[] args) {
		testEncode();
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
//		DecodeParameters parameters = new DecodeParameters();
//		parameters.set
		
		// Preferences
		Preferences.getInstance().setUseAudioChannel(true);
		Preferences.getInstance().setUseMetadataChannel(false);
		Preferences.getInstance().setUseCryptography(false);
		Preferences.getInstance().setCryptographyAlgorithm("com.stegandroid.algorithms.cryptography.AdvancedEncryptionStandard128");
		Preferences.getInstance().setUseVideoChannel(true);
		Preferences.getInstance().setVideoAlgorithm("com.stegandroid.algorithms.steganography.video.H264SteganographyContainerLsb");

		DecodeProcess process = new DecodeProcess();
		process.toString();
		
	}
}
