package keelfy.sea_wars.client.gui.ingame;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gameplay.WorldClient;
import keelfy.sea_wars.client.gui.BaseGUI;
import keelfy.sea_wars.client.gui.IngameGUI;
import keelfy.sea_wars.client.gui.elements.ButtonGUI;
import keelfy.sea_wars.client.gui.utils.GUIHelper;
import keelfy.sea_wars.common.network.packet.play.client.CPacketGameStage;
import keelfy.sea_wars.common.world.EnumGameStage;
import keelfy.sea_wars.common.world.Field;
import keelfy.sea_wars.common.world.Field.CellState;
import keelfy.sea_wars.common.world.WorldSide;
import keelfy.sea_wars.common.world.ship.EnumShipType;

/**
 * @author keelfy
 */
public class PreparationGUI extends BaseGUI {

	private IngameGUI parent;

	private ButtonGUI readyButton;
	private ButtonGUI[] shipButtons = new ButtonGUI[EnumShipType.values().length];

	private int cellMouseOverX;
	private int cellMouseOverY;

	private EnumShipType curentShip;
	private boolean shipVertically;
	private int[] shipsRemained = new int[EnumShipType.values().length];
	private int shipsAmount = 0;

	private boolean ready = false;

	public PreparationGUI(SeaWars sw, IngameGUI parent) {
		super(sw);

		this.parent = parent;

		for (EnumShipType type : EnumShipType.values()) {
			shipsRemained[type.ordinal()] = type.getAmount();
			shipsAmount += type.getAmount();
		}
	}

	@Override
	public void init() {
		super.init();

		this.elements.add(readyButton = new ButtonGUI(5, "Ready", screenWidth / 2 - 200, screenHeight - 60, 400, 50));
		this.readyButton.setEnabled(shipsAmount == 0);
		this.readyButton.setEnabled(!ready);

		for (EnumShipType type : EnumShipType.values()) {
			String name = type.name() + ": " + shipsRemained[type.ordinal()];
			this.elements.add(shipButtons[type.ordinal()] = new ButtonGUI(type.ordinal(), name, screenWidth / 2 + 50, screenHeight / 2 - 200 + 100 * type.ordinal(), 400, 50));
			if (shipsRemained[type.ordinal()] <= 0)
				shipButtons[type.ordinal()].setEnabled(false);
		}
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.drawDefaultBackground();

		if (SeaWars.getInstance().getPlayer() == null)
			return;

		WorldClient world = sw.getPlayer().getWorld();
		WorldSide mySide = sw.getPlayer().getSide();

		int[] cellMouseOver = parent.drawField(mouseX, mouseY, WorldSide.LEFT, world.getField(mySide), true);
		if (cellMouseOver == null) {
			cellMouseOverX = cellMouseOverY = -1;
		} else {
			cellMouseOverX = cellMouseOver[0];
			cellMouseOverY = cellMouseOver[1];
		}

		super.draw(mouseX, mouseY);

		if (curentShip != null) {
			double length = curentShip.getLength() * IngameGUI.CELL_SIZE + 2 * (curentShip.getLength() - 1);
			GUIHelper.drawRect(mouseX, mouseY, !shipVertically ? length : IngameGUI.CELL_SIZE, shipVertically ? length : IngameGUI.CELL_SIZE, Color.BLACK);
		}
	}

	@Override
	public void keyTyped(int key, int keyCode) {
		super.keyTyped(key, keyCode);

		if (key == GLFW.GLFW_KEY_R && curentShip != null) {
			this.shipVertically = !shipVertically;
		}
	}

	@Override
	public boolean mousePressed(double mouseX, double mouseY, int mouseButton) {
		if (curentShip != null && cellMouseOverX != -1 && cellMouseOverY != -1) {
			int lengthX = !shipVertically ? curentShip.getLength() : 1;
			int lengthY = shipVertically ? curentShip.getLength() : 1;

			for (int i = cellMouseOverX - 1; i < cellMouseOverX + lengthX + 1; i++) {
				for (int j = cellMouseOverY - 1; j < cellMouseOverY + lengthY + 1; j++) {
					if (i < 0 || i > Field.FIELD_SIZE - 1 || j < 0 || j > Field.FIELD_SIZE - 1)
						continue;

					if (getMyField().getCellState(i, j) != CellState.NONE) {
						return false;
					}
				}
			}

			for (int i = 0; i < lengthX; i++) {
				for (int j = 0; j < lengthY; j++) {
					if (cellMouseOverX + i > Field.FIELD_SIZE - 1 || cellMouseOverY + j > Field.FIELD_SIZE - 1)
						return false;
				}
			}

			for (int i = 0; i < lengthX; i++) {
				for (int j = 0; j < lengthY; j++) {
					getMyField().setCellState(cellMouseOverX + i, cellMouseOverY + j, CellState.SHIP);
				}
			}

			--shipsRemained[curentShip.ordinal()];
			--shipsAmount;
			curentShip = null;
			this.init();
		}
		return super.mousePressed(mouseX, mouseY, mouseButton);
	}

	private Field getMyField() {
		return sw.getPlayer().getWorld().getField(sw.getPlayer().getSide());
	}

	@Override
	public void actionPerfomed(int elementID, int mode) {
		if (mode != 0)
			return;

		for (EnumShipType type : EnumShipType.values()) {
			if (type.ordinal() == elementID) {
				this.curentShip = type;
				return;
			}
		}

		switch (elementID) {
			case 4 :
				parent.actionPerfomed(0, 0);
				break;
			case 5 :
				sw.getNetHandler().sendPacket(new CPacketGameStage(EnumGameStage.READY));
				ready = true;
				readyButton.setEnabled(false);
				break;
		}
	}

}
