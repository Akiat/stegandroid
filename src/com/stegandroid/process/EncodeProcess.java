package com.stegandroid.process;

import com.stegandroid.algorithms.AlgorithmFactory;
import com.stegandroid.algorithms.IAlgorithm;
import com.stegandroid.configuration.Configuration;

public class EncodeProcess {

	public EncodeProcess() {
		
	}
	
	public void encode(String pathToVideo, String destinationPath) {
		IAlgorithm audioAlgorithm;
		IAlgorithm videoAlgorithm;
		IAlgorithm metadataAlgorithm;
		
		// TO DO: Open the video and extract the channels
		
		if (Configuration.getInstance().getUseAudioChannel()) {
			audioAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getAudioAlgorithm());
			audioAlgorithm.encode();
		}
		
		if (Configuration.getInstance().getUseVideoChannel()) {
			videoAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getVideoAlgorithm());
			videoAlgorithm.encode();
			
		}
		
		if (Configuration.getInstance().getUseMetadataChannel()) {
			System.out.println(Configuration.getInstance().getMetadataAlgorithm());
			metadataAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getMetadataAlgorithm());
			metadataAlgorithm.encode();			
		}
		
		// TO DO: Save the new video
	}
	
	public void decode(String pathToVideo, String destinationPath) {
		IAlgorithm audioAlgorithm;
		IAlgorithm videoAlgorithm;
		IAlgorithm metadataAlgorithm;
		
		// TO DO: Open the video and extract the channels
		
		if (Configuration.getInstance().getUseAudioChannel()) {
			audioAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getAudioAlgorithm());
			audioAlgorithm.decode();
		}
		
		if (Configuration.getInstance().getUseVideoChannel()) {
			videoAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getVideoAlgorithm());
			videoAlgorithm.decode();
			
		}
		
		if (Configuration.getInstance().getUseMetadataChannel()) {
			metadataAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getMetadataAlgorithm());
			metadataAlgorithm.decode();			
		}
		
		// TO DO: Save the new video
	}
	
	
	
}
