package keelfy.sea_wars.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import keelfy.sea_wars.common.world.World;
import keelfy.sea_wars.server.network.NetworkSystem;

/**
 * @author keelfy
 */
public class SeaWarsServer {

	private static final Logger logger = Logger.getLogger(SeaWarsServer.class.getSimpleName());

	private static SeaWarsServer server;

	public static SeaWarsServer getServer() {
		return server;
	}

	public static Logger getLogger() {
		return logger;
	}

	private boolean serverRunning = true;

	private int port;
	private final NetworkSystem network;

	private World world;
	private List<ServerPlayer> players;

	public SeaWarsServer(int port) {
		server = this;

		this.port = port;
		this.network = new NetworkSystem(this);
		this.players = new ArrayList<ServerPlayer>();
	}

	public void run() {
		try {
			if (this.start()) {
				long startTime = System.currentTimeMillis();
				long timeAlive = 0L;

				while (this.serverRunning) {
					long currentTime = System.currentTimeMillis();
					long timeAfterLastLoop = currentTime - startTime;

					if (timeAfterLastLoop < 0L) {
						logger.warn("Time ran backwards");
						timeAfterLastLoop = 0L;
					}

					timeAlive += timeAfterLastLoop;
					startTime = currentTime;

					while (timeAlive > 50L) {
						timeAlive -= 50L;
						this.tick();
					}

					Thread.sleep(Math.max(1L, 50L - timeAlive));
				}
			} else {
				logger.error("Server won't start");
				return;
			}
		} catch (Throwable t) {
			logger.error("Error occurred", t);
		} finally {
			try {
				this.terminate();
			} catch (Throwable t) {
				logger.error("Error occurred while stopping server", t);
			} finally {
				System.exit(-1);
			}
		}
	}

	public boolean start() throws UnknownHostException {
		PropertyConfigurator.configure(SeaWarsServer.class.getResourceAsStream("../log4j.properties"));

		this.world = new World();

		InetAddress address = null;
		logger.info("Starting server on " + (address != null ? address.getHostAddress() : "localhost") + ":" + port);

		try {
			this.getNetwork().addLanEndpoint(address, port);
		} catch (IOException e) {
			logger.error("**** FAILED TO BIND TO PORT!", e);
			return false;
		}

		return true;
	}

	public void tick() {
		if (this.getNetwork() != null) {
			this.getNetwork().networkTick();
		}
	}

	public void terminate() {
		if (this.getNetwork() != null) {
			this.getNetwork().terminateEndpoints();
		}
	}

	public boolean isServerRunning() {
		return serverRunning;
	}

	public void setServerRunning(boolean serverRunning) {
		this.serverRunning = serverRunning;
	}

	public List<ServerPlayer> getPlayers() {
		return players;
	}

	public World getWorld() {
		return world;
	}

	public NetworkSystem getNetwork() {
		return network;
	}

	public int getPort() {
		return port;
	}
}
