package keelfy.sea_wars.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import keelfy.sea_wars.client.gameplay.ClientPlayer;
import keelfy.sea_wars.client.gui.GraphicalUI;
import keelfy.sea_wars.client.gui.MainMenuGUI;
import keelfy.sea_wars.client.gui.font.Fonts;
import keelfy.sea_wars.client.input.KeyboardHandler;
import keelfy.sea_wars.client.input.MouseHandler;
import keelfy.sea_wars.client.main.Main;
import keelfy.sea_wars.client.network.play.NetHandlerPlayClient;
import keelfy.sea_wars.client.settings.SettingsHandler;

/**
 * @author keelfy
 */
public final class SeaWars {

	public static final String NAME = "Sea Wars";
	public static final String VERSION = "1.0.0.0";

	private static SeaWars instance;
	private static Logger logger = Logger.getLogger(SeaWars.class.getSimpleName());
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

	private final DisplayHandler displayHandler;
	private final SettingsHandler settingsHandler;
	private final KeyboardHandler keyboardHandler;
	private final MouseHandler mouseHandler;
	private GraphicalUI currentGUI;

	private String username;
	private ClientPlayer player;
	private NetHandlerPlayClient netHandler;

	public SeaWars() {
		instance = this;
		dataFolder = new File(".");

		this.displayHandler = new DisplayHandler();
		this.settingsHandler = new SettingsHandler(dataFolder);
		this.keyboardHandler = new KeyboardHandler(this);
		this.mouseHandler = new MouseHandler(this);

		String param = System.getProperty("username");
		this.username = param == null ? "Player" : param;
	}

	/**
	 * Called from {@link Main}
	 */
	public void start() {
		try {
			PropertyConfigurator.configure(new FileInputStream(new File(dataFolder, "config" + File.separator + "log4j.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unnable to initialize GLFW");
		}

		this.settingsHandler.preInit();

		this.displayHandler.setSize(settingsHandler.getVideo().getWidth(), settingsHandler.getVideo().getHeight());
		this.displayHandler.init();

		this.openGUI(new MainMenuGUI(this));

		GL.createCapabilities();

		double frameCap = 1.0 / settingsHandler.getVideo().getFramesLimit();
		double frameTime = 0;
		int frames = 0;
		double time = GLFW.glfwGetTime();
		double unprocessed = 0;
		boolean canRender = false;
		int lastKnownFrames = frames;

		long startTime = System.currentTimeMillis();
		long timeAlive = 0L;

		SeaWars.getLogger().info("Starting game loop...");
		while (!displayHandler.shouldClose()) {
			double time2 = GLFW.glfwGetTime();
			double passed = time2 - time;
			unprocessed += passed;
			frameTime += passed;

			time = time2;

			long currentTime = System.currentTimeMillis();
			long timeAfterLastLoop = currentTime - startTime;

			if (timeAfterLastLoop < 0L) {
				logger.warn("Time ran backwards");
				timeAfterLastLoop = 0L;
			}

			timeAlive += timeAfterLastLoop;
			startTime = currentTime;

			GLFW.glfwPollEvents();

			if (timeAlive > 50L) {
				timeAlive = 0;
				this.tick();
			}

			while (unprocessed >= frameCap) {
				unprocessed -= frameCap;
				canRender = true;

				if (frameTime >= 1.0) {
					frameTime = 0;
					lastKnownFrames = frames;
					frames = 0;
				}
			}

			if (!canRender)
				continue;

			int lastWidth = displayHandler.getWidth();
			int lastHeight = displayHandler.getHeight();

			displayHandler.update();

			if (lastWidth != displayHandler.getWidth() || lastHeight != displayHandler.getHeight())
				this.currentGUI.init();

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			if (!Fonts.isLoaded())
				Fonts.create();

			draw();

			displayHandler.swapBuffers();

			frames++;
		}

		this.displayHandler.destroy();
		this.cleanUp();
	}

	public void tick() {
		if (currentGUI != null) {
			this.currentGUI.update();
		}

		if (this.netHandler != null && this.netHandler.getNetworkManager() != null) {
			this.netHandler.getNetworkManager().processReceivedPackets();
		}
	}

	/**
	 * Called from {@link DisplayHandler}
	 */
	public void draw() {
		if (this.currentGUI != null) {
			double[] mousePosition = displayHandler.getMousePosition();
			this.currentGUI.draw(mousePosition[0], mousePosition[1]);
		}
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

	public void exit() {
		logger.info("Exiting...");

		if (this.netHandler != null) {
			this.netHandler.getNetworkManager().closeChannel("Disconnect by user");
		}
		this.displayHandler.close();
	}

	public GraphicalUI getCurrentGUI() {
		return currentGUI;
	}

	public void openGUI(GraphicalUI gui) {
		if (gui == null) {
			gui = new MainMenuGUI(this);
		}

		if (this.currentGUI != null) {
			this.currentGUI.onClose();
		}

		this.currentGUI = gui;

		if (this.currentGUI != null) {
			this.currentGUI.init();
		}
	}

	public NetHandlerPlayClient getNetHandler() {
		return netHandler;
	}

	public void setNetHandler(NetHandlerPlayClient netHandler) {
		this.netHandler = netHandler;
	}

	public DisplayHandler getDisplay() {
		return displayHandler;
	}

	public SettingsHandler getSettings() {
		return settingsHandler;
	}

	public KeyboardHandler getKeyboard() {
		return keyboardHandler;
	}

	public MouseHandler getMouse() {
		return mouseHandler;
	}

	public void setPlayer(ClientPlayer player) {
		this.player = player;
	}

	public ClientPlayer getPlayer() {
		return player;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
