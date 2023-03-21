package gameServer;

import java.net.DatagramSocket;
import java.net.NetworkInterface;

public class GameMaker {
	GameServer game;
	LanDiscoveryServer lanDiscoveryServer;
	public GameMaker(int port, String gameName, int expansionPack, int numberOfBots, int maxPlayers, String joinPassword) {
		game = new GameServer(port);
		lanDiscoveryServer = new LanDiscoveryServer(port, gameName, expansionPack, numberOfBots, maxPlayers, joinPassword, game);
		GameServer.startNewThread(() -> {lanDiscoveryServer.start();});
	}
	public void end() {
		lanDiscoveryServer.stop();
	}
}