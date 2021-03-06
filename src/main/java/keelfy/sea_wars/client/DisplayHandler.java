package keelfy.sea_wars.client;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * @author keelfy
 */
public final class DisplayHandler {

	private static final String TITLE = "Sea Wars";

	private long window;

	private int width;
	private int height;

	void init() {
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

		this.createWindow();

		GLFW.glfwSetKeyCallback(window, SeaWars.getInstance().getKeyboard());
		GLFW.glfwSetMouseButtonCallback(window, SeaWars.getInstance().getMouse());

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
		SeaWars.getLogger().info("Window created");
	}

	void createWindow() throws RuntimeException {
		window = GLFW.glfwCreateWindow(width, height, TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
	}

	void update() {
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(window, widthBuffer, heightBuffer);
		width = widthBuffer.get(0);
		height = heightBuffer.get(0);
		GL11.glViewport(0, 0, width, height);

		double aspectRatio = (double) this.width / (double) this.height;
		GL11.glLoadIdentity();
		GL11.glOrtho(aspectRatio, width, height, 0, 1, -1);
	}

	void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}

	boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}

	void destroy() {
		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
	}

	public boolean isKeyPressed(int keyId) {
		return GLFW.glfwGetKey(window, keyId) == GLFW.GLFW_TRUE;
	}

	public double[] getMousePosition() {
		DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(window, x, y);
		double[] position = new double[2];
		position[0] = x.get();
		position[1] = y.get();
		return position;
	}

	public void close() {
		GLFW.glfwSetWindowShouldClose(window, true);
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public long getWindow() {
		return window;
	}

	public double getAspectRatio() {
		return this.getWidth() / this.height;
	}
}
