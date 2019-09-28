package keelfy.sea_wars.client.display;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import keelfy.sea_wars.client.SeaWars;

/**
 * @author keelfy
 */
public final class DisplayHandler {

	private static final String TITLE = "Sea Wars";

	private final SeaWars seaWars;
	private long window;

	private int width;
	private int height;

	public DisplayHandler(SeaWars seaWars, int width, int height) {
		this.seaWars = seaWars;
		this.width = width;
		this.height = height;
	}

	public void createAndStartLoop() {
		init();
		loop();

		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		SeaWars.getLogger().info("Window destroyed");

		this.seaWars.cleanUp();
		SeaWars.getLogger().info("Memory cleaned up");

		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unnable to initialize GLFW");
		}

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

		window = GLFW.glfwCreateWindow(width, height, TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		SeaWars.getLogger().info("Window created");

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
				GLFW.glfwSetWindowShouldClose(window, true);
		});

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			GLFW.glfwGetWindowSize(window, pWidth, pHeight);

			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			GLFW.glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);

		GLFW.glfwShowWindow(window);
	}

	private void loop() {
		GL.createCapabilities();

		SeaWars.getLogger().info("Starting game loop...");
		while (!GLFW.glfwWindowShouldClose(window)) {
			GLFW.glfwPollEvents();

			IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
			IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetWindowSize(window, widthBuffer, heightBuffer);

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			this.seaWars.draw();

			GLFW.glfwSwapBuffers(window);
		}
	}

	public void close() {
		GLFW.glfwSetWindowShouldClose(window, true);
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
}
