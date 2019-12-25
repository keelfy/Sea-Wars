package keelfy.sea_wars.server.main;

import keelfy.sea_wars.server.SeaWarsServer;

/**
 * @author keelfy
 */
public class Main {

	public static void main(String[] args) {
		SeaWarsServer server = new SeaWarsServer(8090);
		try {
			server.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
