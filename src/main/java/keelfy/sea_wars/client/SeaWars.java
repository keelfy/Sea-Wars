package keelfy.sea_wars.client;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import keelfy.sea_wars.client.gui.Gui;
import keelfy.sea_wars.client.gui.GuiInitialization;
import keelfy.sea_wars.client.main.Main;
import keelfy.sea_wars.client.settings.SettingsHandler;

/**
 * @author keelfy
 */
public final class SeaWars {

	public static final String NAME = "Sea Wars";
	public static final String VERSION = "1.0.0.0";

	private static SeaWars instance;
	private static Logger logger = LogManager.getLogger(NAME);
	private static File dataFolder;

	public static SeaWars getInstance() {
		return instance;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static File getDataFolder() {
		return dataFolder;
	}

	private DisplayHandler display;
	private SettingsHandler settings;
	private Gui currentGUI;

	public SeaWars() {
		instance = this;
		dataFolder = new File("");

		settings = new SettingsHandler(dataFolder);

		display = new DisplayHandler();
	}

	/**
	 * Called from {@link Main}
	 */
	public void start() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unnable to initialize GLFW");
		}

		this.settings.preInit();

		this.display.setSize(settings.getVideo().getWidth(), settings.getVideo().getHeight());
		this.display.init();

		GL.createCapabilities();

		this.currentGUI = new GuiInitialization();

		double frameCap = 1.0 / settings.getVideo().getFramesLimit();
		double frameTime = 0;
		int frames = 0;
		double time = GLFW.glfwGetTime();
		double unprocessed = 0;
		double time2;
		double passed;
		boolean canRender = false;

		SeaWars.getLogger().info("Starting game loop...");
		while (!display.shouldClose()) {
			time2 = GLFW.glfwGetTime();
			passed = time2 - time;
			unprocessed += passed;
			frameTime += passed;

			time = time2;

			while (unprocessed >= frameCap) {
				unprocessed -= frameCap;
				canRender = true;

				GLFW.glfwPollEvents();

				if (frameTime >= 1.0) {
					frameTime = 0;
					logger.info("FPS: " + frames);
					frames = 0;
				}
			}

			if (!canRender)
				continue;

			display.update();

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			draw();

			display.swapBuffers();

			frames++;
		}

		this.display.destroy();
		this.cleanUp();
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
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();

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

	public DisplayHandler getDisplay() {
		return display;
	}

	public SettingsHandler getSettings() {
		return settings;
	}
}
