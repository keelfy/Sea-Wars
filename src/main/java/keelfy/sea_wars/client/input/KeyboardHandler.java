package keelfy.sea_wars.client.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import keelfy.sea_wars.client.SeaWars;

/**
 * @author keelfy
 */
public class KeyboardHandler extends GLFWKeyCallback {

	private final SeaWars sw;

	public KeyboardHandler(SeaWars sw) {
		this.sw = sw;
	}

	@Override
	public void invoke(long window, int key, int scanCode, int action, int mods) {
		if (action == GLFW.GLFW_PRESS) {
			sw.getCurrentGUI().keyTyped(key, scanCode);
		} else if (action == GLFW.GLFW_RELEASE) {

		}
	}

}
