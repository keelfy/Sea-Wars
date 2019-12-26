package keelfy.sea_wars.common.network.util;

import java.io.IOException;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class MessageDeserializer extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> objects) throws IOException {
		int i = buffer.readableBytes();

		if (i != 0) {
			PacketBuffer packetBuffer = new PacketBuffer(buffer);
			int index = packetBuffer.readVarIntFromBuffer();
			Packet packet = Packet.generatePacket(ctx.channel().attr(NetworkManager.attrKeyReceivable).get(), index);

			if (packet == null) {
				throw new IOException("Bad packet id " + index);
			} else {
				packet.readPacketData(packetBuffer);

				if (packetBuffer.readableBytes() > 0) {
					throw new IOException("Packet was larger than I expected, found " + packetBuffer.readableBytes() + " bytes extra whilst reading packet " + index);
				} else {
					objects.add(packet);
				}
			}
		}
	}
}
