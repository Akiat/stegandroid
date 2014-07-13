package com.stegandroid.mp4.video;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReaderVariable;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.h264.AvcConfigurationBox;
import com.coremedia.iso.boxes.mdat.SampleList;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.util.Path;

public class MP4VideoExtracter {

	private static final String VIDEO_TRACKBOX_PATH = "/moov/trak/mdia/minf/stbl/stsd/avc1/../../../../../";
	private static final String VIDEO_CONFIGURATION_BOX_PATH = "mdia/minf/stbl/stsd/avc1/avcC";
	
	public static byte[] getH264VideoAsByteArray(String videoPath) throws IOException {
		IsoFile isoFile;
        TrackBox trackBox; 
        SampleList sampleList;
        ByteArrayOutputStream outputStream;       
		AvcConfigurationBox videoConfigurationBox;
		int lengthSize;
		
        if (videoPath == null || videoPath.isEmpty()) {
			return null;
		}
                
        isoFile = new IsoFile(videoPath);
		trackBox = (TrackBox) Path.getPath(isoFile, VIDEO_TRACKBOX_PATH);
		sampleList = new SampleList(trackBox, new IsoFile[0]);
		outputStream = new ByteArrayOutputStream();
		videoConfigurationBox = (AvcConfigurationBox) Path.getPath(trackBox, VIDEO_CONFIGURATION_BOX_PATH);
		
		outputStream.write(new byte[]{0, 0, 0, 1});
		outputStream.write(ByteBuffer.wrap((videoConfigurationBox).getSequenceParameterSets().get(0)).array());

		outputStream.write(new byte[]{0, 0, 0, 1});
		outputStream.write(ByteBuffer.wrap((videoConfigurationBox).getPictureParameterSets().get(0)).array());

        lengthSize = videoConfigurationBox.getLengthSizeMinusOne() + 1;

        for (Sample sample : sampleList) {
            ByteBuffer buffer = sample.asByteBuffer();

            while (buffer.remaining() > 0) {
                int readlength = (int) IsoTypeReaderVariable.read(buffer, lengthSize);
                outputStream.write(new byte[]{0, 0, 0, 1});
                ByteBuffer byteBuffer = (ByteBuffer) buffer.slice().limit(readlength);	
                byte [] tmp = new byte[byteBuffer.remaining()];
                byteBuffer.get(tmp);
                outputStream.write(tmp);
                buffer.position(buffer.position() + readlength);
            }
        }
        return outputStream.toByteArray();
	}	
	
}
