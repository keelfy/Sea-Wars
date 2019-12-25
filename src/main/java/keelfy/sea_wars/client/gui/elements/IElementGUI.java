package keelfy.sea_wars.client.gui.elements;

import keelfy.sea_wars.client.gui.GraphicalUI;

/**
 * @author keelfy
 */
public interface IElementGUI extends GraphicalUI {

	public int getID();

	public boolean isVisible();

	public boolean isEnabled();

}
