package keelfy.sea_wars.common.world;

/**
 * @author keelfy
 */
public enum WorldSide {
	LEFT, RIGHT;

	public WorldSide getOpposite() {
		return this == LEFT ? RIGHT : LEFT;
	}
}
