package keelfy.sea_wars.client.gameplay;

import keelfy.sea_wars.common.Player;
import keelfy.sea_wars.common.world.World;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class ClientPlayer extends Player {

	public ClientPlayer(String name, WorldSide side, World world) {
		super(name, side, world);
	}

	@Override
	public WorldClient getWorld() {
		return (WorldClient) super.getWorld();
	}

}
