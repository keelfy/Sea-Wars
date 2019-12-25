package keelfy.sea_wars.client.gui;

/**
 * @author keelfy
 */
public interface GraphicalUI {

	public void init();

	public void draw(double mouseX, double mouseY);

	public void update();

	public void onClose();

	public void keyTyped(int key, int keyCode);

	public boolean mousePressed(double mouseX, double mouseY, int mouseButton);

	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton);
}
