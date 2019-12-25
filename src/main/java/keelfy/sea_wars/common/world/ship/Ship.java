package keelfy.sea_wars.common.world.ship;

/**
 * @author keelfy
 */
public class Ship {

	private final int posX;
	private final int posY;

	private final EnumShipType type;

	public Ship(int posX, int posY, EnumShipType type) {
		this.posX = posX;
		this.posY = posY;
		this.type = type;
	}

	public int getX() {
		return posX;
	}

	public int getY() {
		return posY;
	}

	public EnumShipType getType() {
		return type;
	}
}
