package keelfy.sea_wars.server;

import keelfy.sea_wars.common.Player;
import keelfy.sea_wars.common.world.World;
import keelfy.sea_wars.common.world.WorldSide;
import keelfy.sea_wars.server.network.play.NetHandlerPlayServer;

/**
 * @author keelfy
 */
public class ServerPlayer extends Player {

	private NetHandlerPlayServer netHandler;
	private int ping;
	private long lastActionTime = System.currentTimeMillis();

	public ServerPlayer(String name, WorldSide side, World world) {
		super(name, side, world);

		this.ping = 0;
	}

	public static ServerPlayer create(SeaWarsServer server, WorldSide side, String name) {
		ServerPlayer player = new ServerPlayer(name, side, server.getWorld());
		return player;
	}

	public NetHandlerPlayServer getNetHandler() {
		return netHandler;
	}

	public void setNetHandler(NetHandlerPlayServer netHandler) {
		this.netHandler = netHandler;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public long getLastActionTime() {
		return lastActionTime;
	}

	public void setLastActionTime() {
		this.lastActionTime = System.currentTimeMillis();
	}
}
