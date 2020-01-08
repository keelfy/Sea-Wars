package keelfy.sea_wars.client.gui;

import java.util.Map;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gui.elements.ButtonGUI;
import keelfy.sea_wars.client.gui.font.Fonts;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAttack;
import keelfy.sea_wars.common.world.Field;
import keelfy.sea_wars.common.world.Field.CellState;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class ProccessGUI extends BaseGUI {

	private final IngameGUI parent;

	private int[] mouseOverPair = null;

	private boolean myMove;

	public ProccessGUI(SeaWars sw, IngameGUI parent) {
		super(sw);

		this.parent = parent;
	}

	@Override
	public void init() {
		super.init();

		this.myMove = sw.getWorld().getSideOfMove() == sw.getPlayer().getSide();
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		drawDefaultBackground();

		Fonts.drawCenteredString(myMove ? "Your move!" : "Wait for enemy's move...", screenWidth / 2, 70, ButtonGUI.TEXT);

		Map<WorldSide, Field> fields = sw.getWorld().getFields();
		for (WorldSide side : WorldSide.values()) {
			if (fields.containsKey(side)) {
				if (side == sw.getPlayer().getSide().getOpposite()) {
					mouseOverPair = parent.drawField(mouseX, mouseY, side, fields.get(side), myMove);
				} else {
					parent.drawField(mouseX, mouseY, side, fields.get(side), myMove);
				}
			}
		}

		super.draw(mouseX, mouseY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (super.mouseReleased(mouseX, mouseY, mouseButton))
			return true;

		if (mouseOverPair == null || !myMove)
			return false;

		int x = mouseOverPair[0];
		int y = mouseOverPair[1];

		CellState state = sw.getPlayer().getWorld().getField(sw.getPlayer().getSide().getOpposite()).getCellState(x, y);
		if (state == CellState.HIT || state == CellState.MISS)
			return false;

		sw.getNetHandler().sendPacket(new CPacketAttack(x, y));
		return true;
	}

	@Override
	public void actionPerfomed(int elementID, int mode) {}
}
