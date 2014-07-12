package com.stegandroid.process;

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
		byte[] contentToHide;
		byte[] videoSignal;
		byte[] audioSignal;
		byte[] metadataSignal;
		
		if (parameters.isUsingHideText()) {
			contentToHide = parameters.getTextToHide().getBytes();
		} else {
			contentToHide = Utils.getContentOfFileAsByteArray(parameters.getFileToHidePath());
		}
		
		videoSignal = processVideoSignal(parameters, contentToHide);
		audioSignal = processAudioSignal(parameters, contentToHide);
		metadataSignal = processMetadataSignal(parameters, contentToHide);
	}
	
	private byte[] processVideoSignal(EncodeParameters parameters, byte[] contentToHide) {
		ISteganographyAlgorithm videoAlgorithm;
		MP4Exctracter extracter;
		byte[] videoSignal;
		
		if (!Configuration.getInstance().getUseVideoChannel() || contentToHide == null
				|| contentToHide.length == 0) {
			return null;
		}

		extracter = new MP4Exctracter();
		videoSignal = extracter.extractH264(parameters.getSrcVideoPath());
		// TODO: Correct this part (Error management)
		if (videoSignal == null) {
			videoSignal = new byte[0];
		}
		
		videoAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getVideoAlgorithm());
		return videoAlgorithm.encode(videoSignal, contentToHide);
	}
	
	private byte[] processAudioSignal(EncodeParameters parameters, byte[] contentToHide) {
		ISteganographyAlgorithm audioAlgorithm;
		MP4Exctracter extracter;
		byte[] videoSignal;
		
		if (!Configuration.getInstance().getUseAudioChannel() || contentToHide == null
				|| contentToHide.length == 0) {
			return null;
		}

		extracter = new MP4Exctracter();
		videoSignal = extracter.extractH264(parameters.getSrcVideoPath());
		// TODO: Correct this part (Error management)
		if (videoSignal == null) {
			videoSignal = new byte[0];
		}
		
		audioAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getAudioAlgorithm());
		return audioAlgorithm.encode(videoSignal, contentToHide);
	}
	
	private byte[] processMetadataSignal(EncodeParameters parameters, byte[] contentToHide) {
		ISteganographyAlgorithm metadataAlgorithm;
		MP4Exctracter extracter;
		byte[] videoSignal;
		
		if (!Configuration.getInstance().getUseMetadataChannel() || contentToHide == null
				|| contentToHide.length == 0) {
			return null;
		}

		extracter = new MP4Exctracter();
		videoSignal = extracter.extractH264(parameters.getSrcVideoPath());
		// TODO: Correct this part (Error management)
		if (videoSignal == null) {
			videoSignal = new byte[0];
		}
		
		metadataAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getMetadataAlgorithm());
		return metadataAlgorithm.encode(videoSignal, contentToHide);
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
