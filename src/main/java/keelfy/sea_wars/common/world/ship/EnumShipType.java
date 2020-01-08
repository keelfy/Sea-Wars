package keelfy.sea_wars.common.world.ship;

/**
 * @author keelfy
 */
public enum EnumShipType {
	BATTLESHIP(4), CRUISER(3), DESTROYER(2), BOAT(1);

	private static int all = 0;

	private final int length;
	private final int amount;

	private EnumShipType(int length) {
		this.length = length;
		this.amount = ordinal() + 1;
	}

	public int getLength() {
		return length;
	}

	public int getAmount() {
		return amount;
	}

	public static int allLength() {
		int sum = 0;
		for (EnumShipType type : EnumShipType.values()) {
			sum += type.length * type.amount;
		}
		return sum;
	}
}
