package keelfy.sea_wars.common.network.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class MessagePrepender extends MessageToByteEncoder<ByteBuf> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
		int i = in.readableBytes();
		int j = PacketBuffer.getVarIntSize(i);

		if (j > 3) {
			throw new IllegalArgumentException("Unable to fit " + i + " into " + 3);
		} else {
			PacketBuffer packetBuffer = new PacketBuffer(out);
			packetBuffer.ensureWritable(j + i);
			packetBuffer.writeVarIntToBuffer(i);
			packetBuffer.writeBytes(in, in.readerIndex(), i);
		}
	}
}