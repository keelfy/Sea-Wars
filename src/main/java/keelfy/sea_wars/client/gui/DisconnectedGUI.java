package keelfy.sea_wars.client.gui;

import javax.annotation.Nonnull;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gui.elements.ButtonGUI;
import keelfy.sea_wars.client.gui.font.Fonts;

/**
 * @author keelfy
 */
public class DisconnectedGUI extends BaseGUI {

	private String reason;

	public DisconnectedGUI(SeaWars sw, @Nonnull String reason) {
		super(sw);

		this.reason = reason;
	}

	@Override
	public void init() {
		super.init();

		this.elements.add(new ButtonGUI(0, "Back to menu", screenWidth / 2 - 200, screenHeight / 2 + 30, 400, 50));
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.drawDefaultBackground();

		Fonts.drawCenteredString(reason, screenWidth / 2, screenHeight / 2 - 50, ButtonGUI.TEXT);

		super.draw(mouseX, mouseY);
	}

	@Override
	public void actionPerfomed(int elementID, int mode) {
		if (mode != 0)
			return;

		switch (elementID) {
			case 0 :
				sw.openGUI(new MainMenuGUI(sw));
				break;
			default :
				break;
		}
	}

}
