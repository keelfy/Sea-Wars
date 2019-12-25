package keelfy.sea_wars.common;

import keelfy.sea_wars.common.world.World;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class Player {

	private final WorldSide side;
	private final World world;
	private final String name;

	private boolean ready;

	public Player(String name, WorldSide side, World world) {
		this.name = name;
		this.side = side;
		this.world = world;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isReady() {
		return ready;
	}

	public WorldSide getSide() {
		return side;
	}

	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}
}
