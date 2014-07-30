package com.stegandroid.mp4.audio;

import java.nio.ByteBuffer;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.mdat.SampleList;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.util.Path;

public class MP4AudioExtracter {

	private static final String AUDIO_TRACKBOX_PATH = "/moov/trak/mdia/minf/stbl/stsd/mp4a/../../../../../";
	private static final String AUDIO_CONFIGURATION_BOX_PATH = "mdia/minf/stbl/stsd/mp4a/esds";
	
	public static byte[] getAacAudioAsByteArray(String videoPath) throws Exception {
		IsoFile isoFile;
        TrackBox trackBox; 
        SampleList sampleList;
		ESDescriptorBox esDescriptorBox;
		ESDescriptor descriptor;
		AacByteArrayWriter aacByteArrayWriter = null;	
		int channelConfiguration = -1;
		int frequency = -1;
		
        if (videoPath == null || videoPath.isEmpty()) {
			return null;
		}
                
        isoFile = new IsoFile(videoPath);
		trackBox = (TrackBox) Path.getPath(isoFile, AUDIO_TRACKBOX_PATH);
		sampleList = new SampleList(trackBox, new IsoFile[0]);
		esDescriptorBox = (ESDescriptorBox) Path.getPath(trackBox, AUDIO_CONFIGURATION_BOX_PATH);

		if (esDescriptorBox == null || esDescriptorBox.getDescriptor() == null) {
			throw new Exception("Unable to load ESDescriptor boxes");
		}
		descriptor = (ESDescriptor) esDescriptorBox.getDescriptor();
		if (descriptor.getDecoderConfigDescriptor() == null || descriptor.getDecoderConfigDescriptor().getAudioSpecificInfo() == null) {
			throw new Exception("Unable to load config for audio");			
		}
		frequency = descriptor.getDecoderConfigDescriptor().getAudioSpecificInfo().getSamplingFrequency(); 
		channelConfiguration = descriptor.getDecoderConfigDescriptor().getAudioSpecificInfo().getChannelConfiguration();
		
        for (Sample sample : sampleList) {
            ByteBuffer buffer = sample.asByteBuffer();
            
			if (aacByteArrayWriter == null) {
				aacByteArrayWriter = new AacByteArrayWriter(frequency, channelConfiguration);
			}
			aacByteArrayWriter.write(buffer.array(), 0, buffer.capacity());
        }
        return aacByteArrayWriter.getAudioAsByteArray();
	}
	
}
