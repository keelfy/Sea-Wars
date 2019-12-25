package keelfy.sea_wars.common.network;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.handshake.CPacketHandshake;
import keelfy.sea_wars.common.network.packet.login.CPacketLoginStart;
import keelfy.sea_wars.common.network.packet.login.SPacketLogged;
import keelfy.sea_wars.common.network.packet.login.SPacketLogout;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketDisconnect;
import keelfy.sea_wars.common.network.packet.play.server.SPacketJoinGame;
import keelfy.sea_wars.common.network.packet.status.client.CPacketPing;
import keelfy.sea_wars.common.network.packet.status.client.CPacketQuery;
import keelfy.sea_wars.common.network.packet.status.server.SPacketInfo;
import keelfy.sea_wars.common.network.packet.status.server.SPacketPong;

/**
 * @author keelfy
 */
public enum EnumConnectionState {
	HANDSHAKING {
		{
			// Server
			this.addServerbound(0, CPacketHandshake.class);
		}
	},
	PLAY {
		{
			// Client
			this.addClientbound(0, SPacketAlive.class);
			this.addClientbound(1, SPacketJoinGame.class);
			this.addClientbound(2, SPacketDisconnect.class);

			// Server
			this.addServerbound(0, CPacketAlive.class);
		}
	},
	STATUS {
		{
			// Client
			this.addClientbound(0, SPacketInfo.class);
			this.addClientbound(1, SPacketPong.class);

			// Server
			this.addServerbound(0, CPacketQuery.class);
			this.addServerbound(1, CPacketPing.class);
		}
	},
	LOGIN {
		{
			// Client
			this.addClientbound(0, SPacketLogout.class);
			this.addClientbound(1, SPacketLogged.class);

			// Server
			this.addServerbound(0, CPacketLoginStart.class);
		}
	};

	private static final Logger logger = Logger.getLogger(EnumConnectionState.class.getSimpleName());

	private static final Map<Class<? extends Packet>, EnumConnectionState> packetState = Maps.<Class<? extends Packet>, EnumConnectionState>newHashMap();

	private final BiMap<Integer, Class<? extends Packet>> serverPackets;
	private final BiMap<Integer, Class<? extends Packet>> clientPackets;

	private EnumConnectionState() {
		this.serverPackets = HashBiMap.create();
		this.clientPackets = HashBiMap.create();
	}

	protected EnumConnectionState addServerbound(int index, Class<? extends Packet> packetClass) {
		String s;

		if (this.serverPackets.containsKey(index)) {
			s = "Serverbound packet ID " + index + " is already assigned to " + this.serverPackets.get(index) + "; cannot re-assign to " + packetClass;
			logger.fatal(s);
			throw new IllegalArgumentException(s);
		} else if (this.serverPackets.containsValue(packetClass)) {
			s = "Serverbound packet " + packetClass + " is already assigned to ID " + this.serverPackets.inverse().get(packetClass) + "; cannot re-assign to " + index;
			logger.fatal(s);
			throw new IllegalArgumentException(s);
		} else {
			this.serverPackets.put(index, packetClass);
			return this;
		}
	}

	protected EnumConnectionState addClientbound(int index, Class<? extends Packet> packetClass) {
		String s;

		if (this.clientPackets.containsKey(index)) {
			s = "Clientbound packet ID " + index + " is already assigned to " + this.clientPackets.get(index) + "; cannot re-assign to " + packetClass;
			logger.fatal(s);
			throw new IllegalArgumentException(s);
		} else if (this.clientPackets.containsValue(packetClass)) {
			s = "Clientbound packet " + packetClass + " is already assigned to ID " + this.clientPackets.inverse().get(packetClass) + "; cannot re-assign to " + index;
			logger.fatal(s);
			throw new IllegalArgumentException(s);
		} else {
			this.clientPackets.put(index, packetClass);
			return this;
		}
	}

	public BiMap<Integer, Class<? extends Packet>> getServerPackets() {
		return this.serverPackets;
	}

	public BiMap<Integer, Class<? extends Packet>> getClientPackets() {
		return this.clientPackets;
	}

	public BiMap<Integer, Class<? extends Packet>> getPacketsFromClient(boolean clientSide) {
		return clientSide ? this.getClientPackets() : this.getServerPackets();
	}

	public BiMap<Integer, Class<? extends Packet>> getPacketsFromServer(boolean serverSide) {
		return serverSide ? this.getServerPackets() : this.getClientPackets();
	}

	public static EnumConnectionState getState(Packet packet) {
		return packetState.get(packet.getClass());
	}

	static {
		for (EnumConnectionState state : EnumConnectionState.values()) {
			Iterator<Class<? extends Packet>> it = Iterables.concat(state.getClientPackets().values(), state.getServerPackets().values()).iterator();

			while (it.hasNext()) {
				Class<? extends Packet> packetClass = it.next();

				if (packetState.containsKey(packetClass) && packetState.get(packetClass) != state) {
					throw new Error("Packet " + packetClass + " is already assigned to protocol " + packetState.get(packetClass) + " - can\'t reassign to " + state);
				}

				packetState.put(packetClass, state);
			}
		}
	}
}