package com.stegandroid.process;

import java.io.ByteArrayInputStream;

import com.stegandroid.algorithms.AlgorithmFactory;
import com.stegandroid.algorithms.ISteganographyAlgorithm;
import com.stegandroid.configuration.Configuration;
import com.stegandroid.mp4.MP4Exctracter;
import com.stegandroid.parameters.EncodeParameters;
import com.stegandroid.tools.Utils;

public class EncodeProcess {

	public EncodeProcess() {
		
	}
	
	public void encode(EncodeParameters parameters) {
//		byte[] videoBytes;
//		byte[] audioBytes;
//		byte[] metadaBytes;
		byte[] contentToHide;
		ByteArrayInputStream test;
				
		if (parameters.isUsingHideText()) {
			contentToHide = parameters.getTextToHide().getBytes();
		} else {
			contentToHide = Utils.getContentOfFileAsByteArray(parameters.getFileToHidePath());
		}
		
		processVideoSignal(parameters, contentToHide);
		// videoBytes = 
		// audioBytes = processAudioSignal(parameters, contentToHide);
		// metadataBytes = processMetadataSignal(parameteres, contentToHide);
	}
	
	private void processVideoSignal(EncodeParameters parameters, byte[] contentToHide) {
		ISteganographyAlgorithm videoAlgorithm;
		MP4Exctracter extracter;
		byte[] videoArray;
		
		extracter = new MP4Exctracter();
		videoArray = extracter.extractH264(parameters.getSrcVideoPath());
		if (videoArray == null) {
			videoArray = new byte[0];
		}
		if (contentToHide == null || contentToHide.length == 0) {
			return;
		}
		
		if (Configuration.getInstance().getUseVideoChannel()) {
			videoAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getVideoAlgorithm());
			videoAlgorithm.encode(videoArray, contentToHide);
		}
	}
	
	private void processAudioSignal(EncodeParameters parameters, byte[] contentToHide) {
	}
	
	private void processMetadataSignal(EncodeParameters parameters, byte[] contentToHide) {
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//		IAlgorithm audioAlgorithm;
//	IAlgorithm videoAlgorithm;
//	IAlgorithm metadataAlgorithm;
//	
//	// TODO: Open the video and extract the channels
//	
//	if (Configuration.getInstance().getUseAudioChannel()) {
//		audioAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getAudioAlgorithm());
//		audioAlgorithm.encode();
//	}
//	
//	if (Configuration.getInstance().getUseVideoChannel()) {
//		videoAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getVideoAlgorithm());
//		videoAlgorithm.encode();
//		
//	}
//	
//	if (Configuration.getInstance().getUseMetadataChannel()) {
//		metadataAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getMetadataAlgorithm());
//		metadataAlgorithm.encode();			
//	}
	
	// TODO: Save the new video
	//copyContent(pathToVideo, destinationPath);

	
//	private void copyContent(String original, String dest) {
//		FileInputStream fileInputStream;
//		FileOutputStream fileOutputStream;
//		byte [] tmp = new byte[256];
//		int readed;
//		
//		try {
//			fileInputStream = new FileInputStream(new File(original));
//			fileOutputStream = new FileOutputStream(new File(dest + "/lalala.mp4"));
//			
//			while ((readed = fileInputStream.read(tmp)) >= 0) {
//				fileOutputStream.write(tmp, 0, readed);
//			}
//			
//			fileInputStream.close();
//			fileOutputStream.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
}
