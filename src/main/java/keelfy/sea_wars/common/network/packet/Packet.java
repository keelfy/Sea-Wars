package keelfy.sea_wars.common.network.packet;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.common.collect.BiMap;

import io.netty.buffer.ByteBuf;
import keelfy.sea_wars.common.network.INetHandler;

/**
 * @author keelfy
 */
public abstract class Packet {

	private static final Logger logger = Logger.getLogger(Packet.class.getSimpleName());

	public static Packet generatePacket(BiMap<Integer, Class<? extends Packet>> packets, int index) {
		try {
			Class<? extends Packet> packetClass = packets.get(index);
			return packetClass == null ? null : (Packet) packetClass.newInstance();
		} catch (Exception exception) {
			logger.error("Couldn\'t create packet " + index, exception);
			return null;
		}
	}

	public static void writeBlob(ByteBuf buffer, byte[] data) {
		buffer.writeShort(data.length);
		buffer.writeBytes(data);
	}

	public static byte[] readBlob(ByteBuf buffer) throws IOException {
		short blob = buffer.readShort();

		if (blob < 0) {
			throw new IOException("Key so small");
		} else {
			byte[] abyte = new byte[blob];
			buffer.readBytes(abyte);
			return abyte;
		}
	}

	public abstract void readPacketData(PacketBuffer packetBuffer) throws IOException;

	public abstract void writePacketData(PacketBuffer packetBuffer) throws IOException;

	public abstract void processPacket(INetHandler netHandler);

	public boolean hasPriority() {
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public String serialize() {
		return "";
	}
}