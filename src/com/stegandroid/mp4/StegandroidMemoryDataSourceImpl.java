package com.stegandroid.mp4;

import static com.googlecode.mp4parser.util.CastUtils.l2i;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import com.googlecode.mp4parser.DataSource;

public class StegandroidMemoryDataSourceImpl implements DataSource {
	ByteBuffer data;

    public StegandroidMemoryDataSourceImpl(byte[] data) {
        this.data = ByteBuffer.wrap(data);
    }

    public StegandroidMemoryDataSourceImpl(ByteBuffer buffer) {
        this.data = buffer;
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        byte[] buf;
        
        if (data.remaining() == 0) {
        	return -1;
        }
        buf = new byte[Math.min(byteBuffer.remaining(), data.remaining())];
        data.get(buf);
        byteBuffer.put(buf);
        return buf.length;
    }

    public long size() throws IOException {
        return data.capacity();
    }

    public long position() throws IOException {
        return data.position();
    }

    public void position(long nuPos) throws IOException {
        data.position(l2i(nuPos));
    }

    public long transferTo(long position, long count, WritableByteChannel target) throws IOException {
        return target.write((ByteBuffer) ((ByteBuffer) data.position(l2i(position))).slice().limit(l2i(count)));
    }

    public ByteBuffer map(long startPosition, long size) throws IOException {
        return (ByteBuffer) ((ByteBuffer) data.position(l2i(startPosition))).slice().limit(l2i(size));
    }

    public void close() throws IOException {
    	
    }
}
