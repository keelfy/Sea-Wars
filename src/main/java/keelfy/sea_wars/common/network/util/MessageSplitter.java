package keelfy.sea_wars.common.network.util;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class MessageSplitter extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> objects) {
		buffer.markReaderIndex();
		byte[] b1 = new byte[3];

		for (int i = 0; i < b1.length; ++i) {
			if (!buffer.isReadable()) {
				buffer.resetReaderIndex();
				return;
			}

			b1[i] = buffer.readByte();

			if (b1[i] >= 0) {
				PacketBuffer packetBuffer = new PacketBuffer(Unpooled.wrappedBuffer(b1));

				try {
					int intVar = packetBuffer.readVarIntFromBuffer();

					if (buffer.readableBytes() >= intVar) {
						objects.add(buffer.readBytes(intVar));
						return;
					}

					buffer.resetReaderIndex();
				} finally {
					packetBuffer.release();
				}
				return;
			}
		}

		throw new CorruptedFrameException("length wider than 21-bit");
	}
}
