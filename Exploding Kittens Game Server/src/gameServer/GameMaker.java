package gameServer;

import java.net.DatagramSocket;
import java.net.NetworkInterface;

import org.json.JSONObject;

import gameServer.ImplodingDoggosUtils.ClientMessage;
import gameServer.clientSide.RemoteGameMessageAdapter;

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
class BotManager{
	
}
/// Goal: create a playerCommunicator which AI players can use without having to make network requests 
/// to communicate to the server, as this would be slow and inefficient.
class BotPlayerCommunicator extends PlayerCommunicator{
	private Bot parent;
	public BotPlayerCommunicator(Bot parent) {
		super(parent);
	}
	@Override
	public void sendRequestMessage(ClientMessage message) {
		parent.acceptClientMessage(message);
	}
}
abstract class Bot extends Player{
	BotToGameServerAdapter serverMessager; // used to take game actions
	RemoteGameMessageAdapter messageHandler; // used to handle received messages
	
	public Bot() {
		this.userCommunicator = new BotPlayerCommunicator(this);
	}
	
	/// To minimise confusion, and to ensure that caller threads aren't kept waiting on an action,
	/// this function should take a short amount of time to run, i.e. it should only pass the required
	/// information to the bot, not do any processing on it. 
	public void acceptClientMessage(ClientMessage message) {
		GameServer.startNewThread(() -> messageHandler.onMessageReceived(message.cont));
	}
	public void takeTurn() {
		
	}
}
class TestingBot extends Bot{
	public TestingBot() {
		
	}
	@Override
	public void takeTurn() {
		serverMessager.drawCard();
	}
	
	
	class TestingBotMessageHandler implements RemoteGameMessageAdapter{
		private Bot parent;
		public TestingBotMessageHandler(TestingBot parent) {
			this.parent = parent;
		}
		
		@Override
		public void onTurnStarted(int playerId) {
			parent.takeTurn();
		}

		@Override
		public void onCardPlayed(int cardId, int playerId) {}

		@Override
		public void onGameStateReceived(GameState gameState) {}

		@Override
		public void onPlayerJoined(int playerId) {}

		@Override
		public void onPlayerDied(int playerId) {}

		@Override
		public void onCardDrawn(int cardId) {}

		@Override
		public void onCheatGameStateReceived(CheatGameState cheatGameState) {}

		@Override
		public void onMessageFromPeers(JSONObject message) {}

		@Override
		public void onTurnEnded(int playerId) {}
		
	}
}
interface BotToGameServerAdapter{
	// playCard? etc.
	public void drawCard();
}