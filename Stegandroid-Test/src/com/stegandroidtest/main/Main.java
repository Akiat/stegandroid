package com.stegandroidtest.main;

import com.stegandroid.configuration.Preferences;
import com.stegandroid.parameters.EncodeParameters;
import com.stegandroid.process.DecodeProcess;
import com.stegandroid.process.EncodeProcess;

public class Main {

	public static void main(String[] args) {
		testEncode();
		System.out.println("Finished");
	}
	
	public static void testEncode() {
		// Encode parameters
		EncodeParameters parameters = new EncodeParameters();	
		parameters.setCryptographyKey("This is a key!!!");
		parameters.setDestinationVideoDirectory("sample\\");
		parameters.setSourceVideoPath("sample\\Evanescence_-_Bring_Me_To_Life.mp4");
		for (int i = 0; i < 500; ++i) {
			parameters.setTextToHide(parameters.getTextToHide() + "This is a text to hide");
		}
		parameters.setHidingText(true);
		
		// Preferences
		Preferences.getInstance().setUseAudioChannel(true);
		Preferences.getInstance().setAudioAlgorithm("com.stegandroid.algorithms.steganography.audio.AACSteganographyContainerLsb");
		Preferences.getInstance().setUseMetadataChannel(false);
		Preferences.getInstance().setUseCryptography(true);
		Preferences.getInstance().setCryptographyAlgorithm("com.stegandroid.algorithms.cryptography.AdvancedEncryptionStandard128");
		Preferences.getInstance().setUseVideoChannel(false);
		Preferences.getInstance().setVideoAlgorithm("com.stegandroid.algorithms.steganography.video.H264SteganographyContainerLsb");

		// Encode process
		EncodeProcess process = new EncodeProcess();
		process.encode(parameters);
	}
	
	public static void testDecode() {
//		DecodeParameters parameters = new DecodeParameters();
//		parameters.set
		
		// Preferences
		Preferences.getInstance().setUseAudioChannel(false);
		Preferences.getInstance().setUseMetadataChannel(false);
		Preferences.getInstance().setUseCryptography(true);
		Preferences.getInstance().setCryptographyAlgorithm("com.stegandroid.algorithms.cryptography.AdvancedEncryptionStandard128");
		Preferences.getInstance().setUseVideoChannel(true);
		Preferences.getInstance().setVideoAlgorithm("com.stegandroid.algorithms.steganography.video.H264SteganographyContainerLsb");

		DecodeProcess process = new DecodeProcess();
		process.toString();
		
	}
}
