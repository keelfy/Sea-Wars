package keelfy.sea_wars.common.network.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;

/**
 * @author keelfy
 */
public class PacketBuffer extends ByteBuf {

	private final ByteBuf buffer;

	public PacketBuffer(ByteBuf buffer) {
		this.buffer = buffer;
	}

	public static int getVarIntSize(int size) {
		return (size & -128) == 0 ? 1 : ((size & -16384) == 0 ? 2 : ((size & -2097152) == 0 ? 3 : ((size & -268435456) == 0 ? 4 : 5)));
	}

	public int readVarIntFromBuffer() {
		int i = 0;
		int j = 0;
		byte b;

		do {
			b = this.readByte();
			i |= (b & 127) << j++ * 7;

			if (j > 5) {
				throw new RuntimeException("VarInt too big");
			}
		} while ((b & 128) == 128);

		return i;
	}

	public void writeVarIntToBuffer(int value) {
		while ((value & -128) != 0) {
			this.writeByte(value & 127 | 128);
			value >>>= 7;
		}

		this.writeByte(value);
	}

	public String readStringFromBuffer(int length) throws IOException {
		int j = this.readVarIntFromBuffer();

		if (j > length * 4) {
			throw new IOException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + length * 4 + ")");
		} else if (j < 0) {
			throw new IOException("The received encoded string buffer length is less than zero! Weird string!");
		} else {
			String s = new String(this.readBytes(j).array(), Charsets.UTF_8);

			if (s.length() > length) {
				throw new IOException("The received string length is longer than maximum allowed (" + j + " > " + length + ")");
			} else {
				return s;
			}
		}
	}

	public void writeStringToBuffer(String str) throws IOException {
		byte[] abyte = str.getBytes(Charsets.UTF_8);

		if (abyte.length > 32767) {
			throw new IOException("String too big (was " + str.length() + " bytes encoded, max " + 32767 + ")");
		} else {
			this.writeVarIntToBuffer(abyte.length);
			this.writeBytes(abyte);
		}
	}

	public ByteBuf getBuffer() {
		return buffer;
	}

	@Override
	public int capacity() {
		return this.buffer.capacity();
	}

	@Override
	public ByteBuf capacity(int capacity) {
		return this.buffer.capacity(capacity);
	}

	@Override
	public int maxCapacity() {
		return this.buffer.maxCapacity();
	}

	@Override
	public ByteBufAllocator alloc() {
		return this.buffer.alloc();
	}

	@Override
	public ByteOrder order() {
		return this.buffer.order();
	}

	@Override
	public ByteBuf order(ByteOrder order) {
		return this.buffer.order(order);
	}

	@Override
	public ByteBuf unwrap() {
		return this.buffer.unwrap();
	}

	@Override
	public boolean isDirect() {
		return this.buffer.isDirect();
	}

	@Override
	public int readerIndex() {
		return this.buffer.readerIndex();
	}

	@Override
	public ByteBuf readerIndex(int index) {
		return this.buffer.readerIndex(index);
	}

	@Override
	public int writerIndex() {
		return this.buffer.writerIndex();
	}

	@Override
	public ByteBuf writerIndex(int index) {
		return this.buffer.writerIndex(index);
	}

	@Override
	public ByteBuf setIndex(int readerIndex, int writerIndex) {
		return this.buffer.setIndex(readerIndex, writerIndex);
	}

	@Override
	public int readableBytes() {
		return this.buffer.readableBytes();
	}

	@Override
	public int writableBytes() {
		return this.buffer.writableBytes();
	}

	@Override
	public int maxWritableBytes() {
		return this.buffer.maxWritableBytes();
	}

	@Override
	public boolean isReadable() {
		return this.buffer.isReadable();
	}

	@Override
	public boolean isReadable(int size) {
		return this.buffer.isReadable(size);
	}

	@Override
	public boolean isWritable() {
		return this.buffer.isWritable();
	}

	@Override
	public boolean isWritable(int size) {
		return this.buffer.isWritable(size);
	}

	@Override
	public ByteBuf clear() {
		return this.buffer.clear();
	}

	@Override
	public ByteBuf markReaderIndex() {
		return this.buffer.markReaderIndex();
	}

	@Override
	public ByteBuf resetReaderIndex() {
		return this.buffer.resetReaderIndex();
	}

	@Override
	public ByteBuf markWriterIndex() {
		return this.buffer.markWriterIndex();
	}

	@Override
	public ByteBuf resetWriterIndex() {
		return this.buffer.resetWriterIndex();
	}

	@Override
	public ByteBuf discardReadBytes() {
		return this.buffer.discardReadBytes();
	}

	@Override
	public ByteBuf discardSomeReadBytes() {
		return this.buffer.discardSomeReadBytes();
	}

	@Override
	public ByteBuf ensureWritable(int min) {
		return this.buffer.ensureWritable(min);
	}

	@Override
	public int ensureWritable(int min, boolean force) {
		return this.buffer.ensureWritable(min, force);
	}

	@Override
	public boolean getBoolean(int index) {
		return this.buffer.getBoolean(index);
	}

	@Override
	public byte getByte(int index) {
		return this.buffer.getByte(index);
	}

	@Override
	public short getUnsignedByte(int index) {
		return this.buffer.getUnsignedByte(index);
	}

	@Override
	public short getShort(int index) {
		return this.buffer.getShort(index);
	}

	@Override
	public int getUnsignedShort(int index) {
		return this.buffer.getUnsignedShort(index);
	}

	@Override
	public int getMedium(int index) {
		return this.buffer.getMedium(index);
	}

	@Override
	public int getUnsignedMedium(int index) {
		return this.buffer.getUnsignedMedium(index);
	}

	@Override
	public int getInt(int index) {
		return this.buffer.getInt(index);
	}

	@Override
	public long getUnsignedInt(int index) {
		return this.buffer.getUnsignedInt(index);
	}

	@Override
	public long getLong(int index) {
		return this.buffer.getLong(index);
	}

	@Override
	public char getChar(int index) {
		return this.buffer.getChar(index);
	}

	@Override
	public float getFloat(int index) {
		return this.buffer.getFloat(index);
	}

	@Override
	public double getDouble(int index) {
		return this.buffer.getDouble(index);
	}

	@Override
	public ByteBuf getBytes(int index, ByteBuf dst) {
		return this.buffer.getBytes(index, dst);
	}

	@Override
	public ByteBuf getBytes(int index, ByteBuf dst, int length) {
		return this.buffer.getBytes(index, dst, length);
	}

	@Override
	public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
		return this.buffer.getBytes(index, dst, dstIndex, length);
	}

	@Override
	public ByteBuf getBytes(int index, byte[] dst) {
		return this.buffer.getBytes(index, dst);
	}

	@Override
	public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
		return this.buffer.getBytes(index, dst, dstIndex, length);
	}

	@Override
	public ByteBuf getBytes(int index, ByteBuffer dst) {
		return this.buffer.getBytes(index, dst);
	}

	@Override
	public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
		return this.buffer.getBytes(index, out, length);
	}

	@Override
	public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
		return this.buffer.getBytes(index, out, length);
	}

	@Override
	public ByteBuf setBoolean(int index, boolean value) {
		return this.buffer.setBoolean(index, value);
	}

	@Override
	public ByteBuf setByte(int index, int value) {
		return this.buffer.setByte(index, value);
	}

	@Override
	public ByteBuf setShort(int index, int value) {
		return this.buffer.setShort(index, value);
	}

	@Override
	public ByteBuf setMedium(int index, int value) {
		return this.buffer.setMedium(index, value);
	}

	@Override
	public ByteBuf setInt(int index, int value) {
		return this.buffer.setInt(index, value);
	}

	@Override
	public ByteBuf setLong(int index, long value) {
		return this.buffer.setLong(index, value);
	}

	@Override
	public ByteBuf setChar(int index, int value) {
		return this.buffer.setChar(index, value);
	}

	@Override
	public ByteBuf setFloat(int index, float value) {
		return this.buffer.setFloat(index, value);
	}

	@Override
	public ByteBuf setDouble(int index, double value) {
		return this.buffer.setDouble(index, value);
	}

	@Override
	public ByteBuf setBytes(int index, ByteBuf src) {
		return this.buffer.setBytes(index, src);
	}

	@Override
	public ByteBuf setBytes(int index, ByteBuf src, int length) {
		return this.buffer.setBytes(index, src, length);
	}

	@Override
	public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
		return this.buffer.setBytes(index, src, srcIndex, length);
	}

	@Override
	public ByteBuf setBytes(int index, byte[] src) {
		return this.buffer.setBytes(index, src);
	}

	@Override
	public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
		return this.buffer.setBytes(index, src, srcIndex, length);
	}

	@Override
	public ByteBuf setBytes(int index, ByteBuffer src) {
		return this.buffer.setBytes(index, src);
	}

	@Override
	public int setBytes(int index, InputStream in, int length) throws IOException {
		return this.buffer.setBytes(index, in, length);
	}

	@Override
	public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
		return this.buffer.setBytes(index, in, length);
	}

	@Override
	public ByteBuf setZero(int index, int length) {
		return this.buffer.setZero(index, length);
	}

	@Override
	public boolean readBoolean() {
		return this.buffer.readBoolean();
	}

	@Override
	public byte readByte() {
		return this.buffer.readByte();
	}

	@Override
	public short readUnsignedByte() {
		return this.buffer.readUnsignedByte();
	}

	@Override
	public short readShort() {
		return this.buffer.readShort();
	}

	@Override
	public int readUnsignedShort() {
		return this.buffer.readUnsignedShort();
	}

	@Override
	public int readMedium() {
		return this.buffer.readMedium();
	}

	@Override
	public int readUnsignedMedium() {
		return this.buffer.readUnsignedMedium();
	}

	@Override
	public int readInt() {
		return this.buffer.readInt();
	}

	@Override
	public long readUnsignedInt() {
		return this.buffer.readUnsignedInt();
	}

	@Override
	public long readLong() {
		return this.buffer.readLong();
	}

	@Override
	public char readChar() {
		return this.buffer.readChar();
	}

	@Override
	public float readFloat() {
		return this.buffer.readFloat();
	}

	@Override
	public double readDouble() {
		return this.buffer.readDouble();
	}

	@Override
	public ByteBuf readBytes(int length) {
		return this.buffer.readBytes(length);
	}

	@Override
	public ByteBuf readSlice(int length) {
		return this.buffer.readSlice(length);
	}

	@Override
	public ByteBuf readBytes(ByteBuf dst) {
		return this.buffer.readBytes(dst);
	}

	@Override
	public ByteBuf readBytes(ByteBuf dst, int length) {
		return this.buffer.readBytes(dst, length);
	}

	@Override
	public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_) {
		return this.buffer.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
	}

	@Override
	public ByteBuf readBytes(byte[] p_readBytes_1_) {
		return this.buffer.readBytes(p_readBytes_1_);
	}

	@Override
	public ByteBuf readBytes(byte[] p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_) {
		return this.buffer.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
	}

	@Override
	public ByteBuf readBytes(ByteBuffer p_readBytes_1_) {
		return this.buffer.readBytes(p_readBytes_1_);
	}

	@Override
	public ByteBuf readBytes(OutputStream p_readBytes_1_, int p_readBytes_2_) throws IOException {
		return this.buffer.readBytes(p_readBytes_1_, p_readBytes_2_);
	}

	@Override
	public int readBytes(GatheringByteChannel p_readBytes_1_, int p_readBytes_2_) throws IOException {
		return this.buffer.readBytes(p_readBytes_1_, p_readBytes_2_);
	}

	@Override
	public ByteBuf skipBytes(int value) {
		return this.buffer.skipBytes(value);
	}

	@Override
	public ByteBuf writeBoolean(boolean value) {
		return this.buffer.writeBoolean(value);
	}

	@Override
	public ByteBuf writeByte(int value) {
		return this.buffer.writeByte(value);
	}

	@Override
	public ByteBuf writeShort(int value) {
		return this.buffer.writeShort(value);
	}

	@Override
	public ByteBuf writeMedium(int value) {
		return this.buffer.writeMedium(value);
	}

	@Override
	public ByteBuf writeInt(int value) {
		return this.buffer.writeInt(value);
	}

	@Override
	public ByteBuf writeLong(long value) {
		return this.buffer.writeLong(value);
	}

	@Override
	public ByteBuf writeChar(int value) {
		return this.buffer.writeChar(value);
	}

	@Override
	public ByteBuf writeFloat(float value) {
		return this.buffer.writeFloat(value);
	}

	@Override
	public ByteBuf writeDouble(double value) {
		return this.buffer.writeDouble(value);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf buffer) {
		return this.buffer.writeBytes(buffer);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_) {
		return this.buffer.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_) {
		return this.buffer.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
	}

	@Override
	public ByteBuf writeBytes(byte[] p_writeBytes_1_) {
		return this.buffer.writeBytes(p_writeBytes_1_);
	}

	@Override
	public ByteBuf writeBytes(byte[] p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_) {
		return this.buffer.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
	}

	@Override
	public ByteBuf writeBytes(ByteBuffer src) {
		return this.buffer.writeBytes(src);
	}

	@Override
	public int writeBytes(InputStream in, int length) throws IOException {
		return this.buffer.writeBytes(in, length);
	}

	@Override
	public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
		return this.buffer.writeBytes(in, length);
	}

	@Override
	public ByteBuf writeZero(int length) {
		return this.buffer.writeZero(length);
	}

	@Override
	public int indexOf(int fromIndex, int toIndex, byte value) {
		return this.buffer.indexOf(fromIndex, toIndex, value);
	}

	@Override
	public int bytesBefore(byte value) {
		return this.buffer.bytesBefore(value);
	}

	@Override
	public int bytesBefore(int length, byte value) {
		return this.buffer.bytesBefore(length, value);
	}

	@Override
	public int bytesBefore(int index, int length, byte value) {
		return this.buffer.bytesBefore(index, length, value);
	}

	@Override
	public ByteBuf copy() {
		return this.buffer.copy();
	}

	@Override
	public ByteBuf copy(int index, int length) {
		return this.buffer.copy(index, length);
	}

	@Override
	public ByteBuf slice() {
		return this.buffer.slice();
	}

	@Override
	public ByteBuf slice(int index, int length) {
		return this.buffer.slice(index, length);
	}

	@Override
	public ByteBuf duplicate() {
		return this.buffer.duplicate();
	}

	@Override
	public int nioBufferCount() {
		return this.buffer.nioBufferCount();
	}

	@Override
	public ByteBuffer nioBuffer() {
		return this.buffer.nioBuffer();
	}

	@Override
	public ByteBuffer nioBuffer(int index, int length) {
		return this.buffer.nioBuffer(index, length);
	}

	@Override
	public ByteBuffer internalNioBuffer(int index, int length) {
		return this.buffer.internalNioBuffer(index, length);
	}

	@Override
	public ByteBuffer[] nioBuffers() {
		return this.buffer.nioBuffers();
	}

	@Override
	public ByteBuffer[] nioBuffers(int index, int length) {
		return this.buffer.nioBuffers(index, length);
	}

	@Override
	public boolean hasArray() {
		return this.buffer.hasArray();
	}

	@Override
	public byte[] array() {
		return this.buffer.array();
	}

	@Override
	public int arrayOffset() {
		return this.buffer.arrayOffset();
	}

	@Override
	public boolean hasMemoryAddress() {
		return this.buffer.hasMemoryAddress();
	}

	@Override
	public long memoryAddress() {
		return this.buffer.memoryAddress();
	}

	@Override
	public String toString(Charset charset) {
		return this.buffer.toString(charset);
	}

	@Override
	public String toString(int index, int length, Charset charset) {
		return this.buffer.toString(index, length, charset);
	}

	@Override
	public int hashCode() {
		return this.buffer.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.buffer.equals(obj);
	}

	@Override
	public int compareTo(ByteBuf buffer) {
		return this.buffer.compareTo(buffer);
	}

	@Override
	public String toString() {
		return this.buffer.toString();
	}

	@Override
	public ByteBuf retain(int increment) {
		return this.buffer.retain(increment);
	}

	@Override
	public ByteBuf retain() {
		return this.buffer.retain();
	}

	@Override
	public int refCnt() {
		return this.buffer.refCnt();
	}

	@Override
	public boolean release() {
		return this.buffer.release();
	}

	@Override
	public boolean release(int decrement) {
		return this.buffer.release(decrement);
	}

	@Override
	public int forEachByte(ByteBufProcessor processor) {
		return this.buffer.forEachByte(processor);
	}

	@Override
	public int forEachByte(int index, int length, ByteBufProcessor processor) {
		return this.buffer.forEachByte(index, length, processor);
	}

	@Override
	public int forEachByteDesc(ByteBufProcessor processor) {
		return this.buffer.forEachByteDesc(processor);
	}

	@Override
	public int forEachByteDesc(int index, int length, ByteBufProcessor processor) {
		return this.buffer.forEachByteDesc(index, length, processor);
	}
}