package com.stegandroid.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
		
		// TODO: Open the video and extract the channels
		
		if (Configuration.getInstance().getUseAudioChannel()) {
			audioAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getAudioAlgorithm());
			audioAlgorithm.encode();
		}
		
		if (Configuration.getInstance().getUseVideoChannel()) {
			videoAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getVideoAlgorithm());
			videoAlgorithm.encode();
			
		}
		
		if (Configuration.getInstance().getUseMetadataChannel()) {
			metadataAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getMetadataAlgorithm());
			metadataAlgorithm.encode();			
		}
		
		// TODO: Save the new video
		copyContent(pathToVideo, destinationPath);

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
	
	private void copyContent(String original, String dest) {
		FileInputStream fileInputStream;
		FileOutputStream fileOutputStream;
		byte [] tmp = new byte[256];
		int readed;
		
		try {
			fileInputStream = new FileInputStream(new File(original));
			fileOutputStream = new FileOutputStream(new File(dest + "/lalala.mp4"));
			
			while ((readed = fileInputStream.read(tmp)) >= 0) {
				fileOutputStream.write(tmp, 0, readed);
			}
			
			fileInputStream.close();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
