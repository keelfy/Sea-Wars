package keelfy.sea_wars.common.network.util;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class MessageSerializer extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet in, ByteBuf out) throws IOException {
		Integer index = ctx.channel().attr(NetworkManager.attrKeySendable).get().inverse().get(in.getClass());

		if (index == null) {
			throw new IOException("Can\'t serialize unregistered packet");
		} else {
			PacketBuffer packetBuffer = new PacketBuffer(out);
			packetBuffer.writeVarIntToBuffer(index);
			in.writePacketData(packetBuffer);
		}
	}
}