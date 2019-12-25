package keelfy.sea_wars.client.gui;

import org.lwjgl.opengl.GL11;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gui.elements.ButtonGUI;
import keelfy.sea_wars.client.gui.font.Fonts;

/**
 * @author keelfy
 */
public class MainMenuGUI extends BaseGUI {

	public MainMenuGUI(SeaWars sw) {
		super(sw);
	}

	@Override
	public void init() {
		super.init();

		this.elements.add(new ButtonGUI(0, "Connect to server", screenWidth / 2 - 200, screenHeight / 2 + 25, 400, 50));
		this.elements.add(new ButtonGUI(1, "Exit", screenWidth / 2 - 200, screenHeight / 2 + 105, 400, 50));
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.drawDefaultBackground();

		float scale = 1.5F;
		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, scale);
		Fonts.drawCenteredString("Sea Wars Game", screenWidth / 2 / scale, (screenHeight / 2 - 150) / scale, ButtonGUI.TEXT);
		GL11.glPopMatrix();

		super.draw(mouseX, mouseY);
	}

	@Override
	public void actionPerfomed(int elementID, int mode) {
		if (mode != 0)
			return;

		switch (elementID) {
			case 0 :
				String address = "127.0.0.1";
				int port = 8090;
				sw.openGUI(new ConnectingGUI(sw, address, port));
				break;
			case 1 :
				sw.exit();
				break;
		}
	}
}
