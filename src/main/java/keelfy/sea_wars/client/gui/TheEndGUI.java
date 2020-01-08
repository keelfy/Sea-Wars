package keelfy.sea_wars.client.gui;

import java.util.Map;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gui.elements.ButtonGUI;
import keelfy.sea_wars.client.gui.font.Fonts;
import keelfy.sea_wars.common.world.Field;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class TheEndGUI extends BaseGUI {

	private final IngameGUI parent;

	public TheEndGUI(SeaWars sw, IngameGUI parent) {
		super(sw);

		this.parent = parent;
	}

	@Override
	public void init() {
		super.init();

		this.elements.add(new ButtonGUI(0, "Leave from server", screenWidth / 2 - 200, screenHeight - 60, 400, 50));
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		drawDefaultBackground();

		Fonts.drawCenteredString("Winner is " + sw.getWorld().getWinner(), screenWidth / 2, 70, ButtonGUI.TEXT);

		Map<WorldSide, Field> fields = sw.getWorld().getFields();
		for (WorldSide side : WorldSide.values()) {
			if (fields.containsKey(side)) {
				parent.drawField(mouseX, mouseY, side, fields.get(side), true);
			}
		}

		super.draw(mouseX, mouseY);
	}

	@Override
	public void actionPerfomed(int elementID, int mode) {
		if (mode != 0)
			return;

		switch (elementID) {
			case 0 :
				sw.getNetHandler().getNetworkManager().closeChannel("Disconnected by user");
				sw.openGUI(new MainMenuGUI(sw));
				sw.setPlayer(null);
				break;
		}
	}
}
