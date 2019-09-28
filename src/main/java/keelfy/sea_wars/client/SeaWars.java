package keelfy.sea_wars.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import keelfy.sea_wars.client.gui.Gui;
import keelfy.sea_wars.client.gui.GuiInitialization;
import keelfy.sea_wars.client.gui.utils.Timer;
import keelfy.sea_wars.client.main.Main;

/**
 * @author keelfy
 */
public final class SeaWars {

	public static final String NAME = "Sea Wars";
	public static final String VERSION = "1.0.0.0";

	private static SeaWars instance;
	private static Logger logger = LogManager.getLogger(NAME);

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

		display = new DisplayHandler(640, 480);
	}

	/**
	 * Called from {@link Main}
	 */
	public void start() {
		this.currentGUI = new GuiInitialization();

		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unnable to initialize GLFW");
		}

		this.display.init();

		GL.createCapabilities();

		double frameCap = 1.0 / 60.0;
		double frameTime = 0;
		int frames = 0;
		double time = Timer.getTime();
		double unprocessed = 0;
		double time2;
		double passed;
		boolean canRender = false;

		SeaWars.getLogger().info("Starting game loop...");
		while (!display.shouldClose()) {
			time2 = Timer.getTime();
			passed = time2 - time;
			unprocessed += passed;
			frameTime += passed;

			time = time2;

			while (unprocessed >= frameCap) {
				unprocessed -= frameCap;
				canRender = true;

				if (display.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
					display.close();
				}

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
}