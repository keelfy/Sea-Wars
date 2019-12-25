package keelfy.sea_wars.client.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gui.elements.ButtonGUI;
import keelfy.sea_wars.client.gui.font.Fonts;
import keelfy.sea_wars.client.network.login.NetHandlerLoginClient;
import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.handshake.CPacketHandshake;
import keelfy.sea_wars.common.network.packet.login.CPacketLoginStart;

/**
 * @author keelfy
 */
public class ConnectingGUI extends BaseGUI {

	private static final ExecutorService serverConnector = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder().setNameFormat("connector-%d").setDaemon(true).build());

	private volatile boolean canceled;
	private NetworkManager networkManager;

	private String address;
	private int port;

	public ConnectingGUI(SeaWars sw, String address, int port) {
		super(sw);

		this.address = address;
		this.port = port;

		this.canceled = false;
		this.connectToServer(address, port);
	}

	@Override
	public void init() {
		super.init();

		this.elements.add(new ButtonGUI(0, "Cancel", screenWidth / 2 - 200, screenHeight / 2 + 30, 400, 50));
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.drawDefaultBackground();

		Fonts.drawCenteredString("Connecting to " + address + ":" + port, screenWidth / 2, screenHeight / 2 - 50, ButtonGUI.TEXT);

		super.draw(mouseX, mouseY);
	}

	@Override
	public void actionPerfomed(int elementID, int mode) {
		if (mode != 0)
			return;

		switch (elementID) {
			case 0 :
				this.canceled = true;
				break;
		}
	}

	private void connectToServer(String address, int port) {
		SeaWars.getLogger().info("Connecting to " + address + ":" + port);

		serverConnector.submit(() -> {
			InetAddress netAddress = null;

			try {
				if (canceled) {
					return;
				}

				netAddress = InetAddress.getByName(address);
				networkManager = NetworkManager.provideLanClient(netAddress, port);
				networkManager.setNetHandler(new NetHandlerLoginClient(sw, networkManager));
				networkManager.scheduleOutboundPacket(new CPacketHandshake(address, port, EnumConnectionState.LOGIN));
				networkManager.scheduleOutboundPacket(new CPacketLoginStart(sw.getUsername()));
			} catch (UnknownHostException e) {
				if (canceled) {
					return;
				}

				String reason = "Couldn\'t connect to server";
				SeaWars.getLogger().error(reason, e);
				sw.openGUI(new DisconnectedGUI(sw, reason));
			} catch (Exception e) {
				if (canceled) {
					return;
				}

				String reason = "Couldn\'t connect to server";
				SeaWars.getLogger().error(reason, e);
				String s = e.toString();

				if (netAddress != null) {
					String s1 = netAddress.toString() + ":" + port;
					s = s.replaceAll(s1, "");
				}

				sw.openGUI(new DisconnectedGUI(sw, reason));
			}
		});
	}

	@Override
	public void update() {
		if (this.networkManager != null) {
			if (this.networkManager.isChannelOpen()) {
				this.networkManager.processReceivedPackets();
			} else if (this.networkManager.getTerminationReason() != null) {
				this.networkManager.getNetHandler().onDisconnect(this.networkManager.getTerminationReason());
			}
		}
	}
}
