package gameServer;

import java.net.DatagramSocket;
import java.net.NetworkInterface;

import org.json.JSONObject;

import gameServer.ImplodingDoggosUtils.ClientMessage;
import gameServer.ImplodingDoggosUtils.PlayCardArgs;
import gameServer.clientSide.RemoteGameMessageAdapter;

public class GameMaker {
	GameServer game;
	LanDiscoveryServer lanDiscoveryServer;
	Thread initGameThread;
	public GameMaker(int port, String gameName, int expansionPack, int numberOfBots, int maxPlayers, String joinPassword) {
		game = new GameServer(port);
		lanDiscoveryServer = new LanDiscoveryServer(port, gameName, expansionPack, numberOfBots, maxPlayers, joinPassword, game);
		GameServer.startNewThread(() -> {lanDiscoveryServer.start();});
		
		//initGameThread = new Thread(() -> {game.init();});
		//initGameThread.start();
		
		for(int i = 0; i < numberOfBots; i++)
			EventSystem.playerConnected.invoke(new TestingBot(game));
	}
	public void end() {
		lanDiscoveryServer.stop();
	}
	public void startGame() {
		game.init();
		game.startServer();
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
		this.parent = parent;
	}
	@Override
	public void sendRequestMessage(ClientMessage message) {
		parent.acceptClientMessage(message);
	}
}
abstract class Bot extends Player{
	BotToGameServerAdapter serverMessager; // used to take game actions
	RemoteGameMessageAdapter messageHandler; // used to handle received messages
	GameServer game;
	
	public Bot(GameServer game) {
		this.game = game;
		this.userCommunicator = new BotPlayerCommunicator(this);
		this.serverMessager = new BotServerMessager(this);
		//serverMessager.joinGame();
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
	public TestingBot(GameServer game) {
		super(game);
		this.messageHandler = new TestingBotMessageHandler(this);
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
		public void onGameStateReceived(GameState gameState) {
		}

		@Override
		public void onPlayerJoined(int playerId) {}

		@Override
		public void onPlayerDied(int playerId) {}

		@Override
		public void onCardDrawn(int playerId, int cardId) {}

		@Override
		public void onCheatGameStateReceived(CheatGameState cheatGameState) {}

		@Override
		public void onTurnEnded(int playerId) {}

		@Override
		public void onMessageFromPeers(String message) {}
		
	}
}
interface BotToGameServerAdapter{
	public void drawCard();
	public GameState getGameState();
	public void playCard(Card card, Object[] args);
	public void joinGame();
	
	public default void playCard(Card card) {playCard(card, new Object[] {});};
	public default void playCard(int cardId) {
		playCard(Card.getCardById(cardId));
	}
	public default void playAtPlayer(Card card, Player player) {
		playCard(card, new Object[] {player.playerId});
	}
}
class BotServerMessager implements BotToGameServerAdapter{
	GameServer game;
	Bot parent;
	public BotServerMessager(Bot player) {
		this.game = player.game;
		this.parent = player;
	}
	@Override
	public void drawCard() {
		game.requestHandler.onRequestDrawCard(parent);
		
	}
	public GameState getGameState() {
		return game.getGameState(parent);
	}
	@Override
	public void playCard(Card card, Object[] args) {
		game.requestHandler.onRequestPlayCard(new PlayCardArgs(parent,card, args));
	}
	@Override
	public void joinGame() {
		EventSystem.playerConnected.invoke(parent);
	}
}