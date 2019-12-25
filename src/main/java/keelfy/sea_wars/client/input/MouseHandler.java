package keelfy.sea_wars.client.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import keelfy.sea_wars.client.SeaWars;

/**
 * @author keelfy
 */
public class MouseHandler extends GLFWMouseButtonCallback {

	private final SeaWars sw;

	public MouseHandler(SeaWars sw) {
		this.sw = sw;
	}

	@Override
	public void invoke(long window, int button, int action, int mods) {
		if (action == GLFW.GLFW_PRESS) {
			sw.getCurrentGUI().mousePressed(sw.getDisplay().getMousePosition()[0], sw.getDisplay().getMousePosition()[1], button);
		} else if (action == GLFW.GLFW_RELEASE) {
			sw.getCurrentGUI().mouseReleased(sw.getDisplay().getMousePosition()[0], sw.getDisplay().getMousePosition()[1], button);
		}
	}
}
