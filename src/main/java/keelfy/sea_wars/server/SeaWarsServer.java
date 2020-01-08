package keelfy.sea_wars.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import keelfy.sea_wars.common.world.World;
import keelfy.sea_wars.common.world.WorldSide;
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
	private Set<String> commandsToExecute;

	private World world;
	private List<ServerPlayer> players;

	public SeaWarsServer(int port) {
		server = this;

		this.port = port;
		this.network = new NetworkSystem(this);
		this.players = new ArrayList<ServerPlayer>();
		this.commandsToExecute = ConcurrentHashMap.newKeySet();
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
		try {
			PropertyConfigurator.configure(new FileInputStream(new File("./config/", "log4j.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		this.world = new World();

		InetAddress address = null;
		logger.info("Starting server on " + (address != null ? address.getHostAddress() : "localhost") + ":" + port);

		try {
			this.getNetwork().addLanEndpoint(address, port);
		} catch (IOException e) {
			logger.error("**** FAILED TO BIND TO PORT!", e);
			return false;
		}

		Thread consoleReader = new Thread("Console reader") {
			@Override
			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String line;

				try {
					while (serverRunning && (line = reader.readLine()) != null) {
						commandsToExecute.add(line);
					}
				} catch (IOException e) {
					logger.error("Exception handling console input", e);
				}
			}
		};
		consoleReader.setDaemon(true);
		consoleReader.start();

		return true;
	}

	public void tick() {
		Iterator<String> it = this.commandsToExecute.iterator();
		while (it.hasNext()) {
			String command = it.next();
			if (command.contains("stop")) {
				logger.info("Server stopped!");
				this.setServerRunning(false);
			}
			it.remove();
		}

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

	public ServerPlayer getPlayerBySide(WorldSide side) {
		for (ServerPlayer player : players) {
			if (player.getSide() == side) {
				return player;
			}
		}
		return null;
	}

	public NetworkSystem getNetwork() {
		return network;
	}

	public int getPort() {
		return port;
	}
}
