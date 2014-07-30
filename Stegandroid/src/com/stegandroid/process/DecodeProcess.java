package com.stegandroid.process;

import com.stegandroid.algorithms.AlgorithmFactory;
import com.stegandroid.algorithms.ISteganographyAlgorithm;
import com.stegandroid.configuration.Preferences;

public class DecodeProcess {

//	public void decode(String pathToVideo, String destinationPath) {
//		IAlgorithm audioAlgorithm;
//		IAlgorithm videoAlgorithm;
//		IAlgorithm metadataAlgorithm;
//		
//		// TO DO: Open the video and extract the channels
//		
//		if (Configuration.getInstance().getUseAudioChannel()) {
//			audioAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getAudioAlgorithm());
//			audioAlgorithm.decode();
//		}
//		
//		if (Configuration.getInstance().getUseVideoChannel()) {
//			videoAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getVideoAlgorithm());
//			videoAlgorithm.decode();
//			
//		}
//		
//		if (Configuration.getInstance().getUseMetadataChannel()) {
//			metadataAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getMetadataAlgorithm());
//			metadataAlgorithm.decode();			
//		}
//
//		// TO DO: Save the new video
//		
//	}
}
