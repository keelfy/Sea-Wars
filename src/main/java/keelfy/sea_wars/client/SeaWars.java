package keelfy.sea_wars.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import keelfy.sea_wars.client.display.DisplayHandler;
import keelfy.sea_wars.client.gui.Gui;
import keelfy.sea_wars.client.gui.GuiInitialization;
import keelfy.sea_wars.client.main.Main;

/**
 * @author keelfy
 */
public final class SeaWars {

	private static SeaWars instance;
	private static Logger logger = LogManager.getLogger("Sea Wars");

	public static SeaWars getInstance() {
		return instance;
	}

	public static Logger getLogger() {
		return logger;
	}

	private DisplayHandler display;

	private Gui currentGUI;

	public SeaWars() {
		instance = this;

		display = new DisplayHandler(this, 640, 480);
	}

	/**
	 * Called from {@link Main}
	 */
	public void start() {
		this.currentGUI = new GuiInitialization();

		this.display.createAndStartLoop();
	}

	/**
	 * Called from {@link DisplayHandler}
	 */
	public void draw() {
		GL11.glPushMatrix();

		if (this.currentGUI != null)
			this.currentGUI.draw();

		GL11.glPopMatrix();
	}

	/**
	 * Called from {@link DisplayHandler}
	 */
	public void cleanUp() {
		if (this.currentGUI != null) {
			this.currentGUI.onClose();
			this.currentGUI = null;
		}
	}

	public Gui getCurrentGUI() {
		return currentGUI;
	}

	public void openGUI(Gui gui) {
		if (this.currentGUI != null) {
			this.currentGUI.onClose();
		}

		this.currentGUI = gui;
	}
}
