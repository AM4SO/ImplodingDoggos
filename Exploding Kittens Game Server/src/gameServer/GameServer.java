package gameServer;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;

import gameServer.ImplodingDoggosUtils.ClientMessage;
import gameServer.ImplodingDoggosUtils.ClientMessageContent;
import gameServer.ImplodingDoggosUtils.ClientMessageType;
import gameServer.ImplodingDoggosUtils.Functions;
import gameServer.ImplodingDoggosUtils.MultiSetterBooleanVariable;
import gameServer.ImplodingDoggosUtils.PlayCardArgs;
import gameServer.ImplodingDoggosUtils.PlayerRequestPair;
import gameServer.ImplodingDoggosUtils.PlrCardPair;

public class GameServer {
	static final boolean debug = true;
	static final int minPlayers = 2;
	
	static GameServer game;
	public static Random random = new Random();
	public ArrayList<Player> players;
	public CardStack drawPile;
	public int playerGo = 0;
	public ArrayList<Card> disposePile;
	public boolean waitingForPlayers = true;
	public MultiSetterBooleanVariable playerJoined;
	public RequestProcessor requestHandler;
	ServerListener listener;
	Thread playerJoinThread;
	
	public GameState getGameState(Player player) { // function to get a serializable object 
		// description of the game to send to the client. 
		GameState gameState = new GameState();
		gameState.cards = Card.getAllCardStates();
		gameState.localPlayerHand = player.cards.getHandState();
		gameState.playerTurn = playerGo;
		gameState.players = new ArrayList<PlayerState>();
		for (Player plr : players) gameState.players.add(plr.getPlayerState(false));
		
		return gameState;
	}
	
	public GameServer(int port) {
		GameServer.game = this;
		requestHandler = new RequestHandler(this);
		playerJoined = new MultiSetterBooleanVariable(false);
		EventSystem.Initialise();
		disposePile = new ArrayList<Card>();
		players = new ArrayList<Player>();
		
		listener = new ServerListener(port);
		System.out.println("Starting listener");
		listener.start();
		
		playerJoinThread = new Thread(() -> allowPlayerJoining());
		playerJoinThread.start();
	}
	public void allowPlayerJoining() { // a function which blocks off until no longer waiting 
		// for players to join the game. When players try to connect, it will add them to the 
		// list of players.
		boolean enoughPlayers = false;
		while (waitingForPlayers) { // Pause thread, tell other thread to awake this thread in x time, and tell otherer thread to
			///                     Tell this thread when plr joined. -- Thread.Notify();
			int waitTime = 10000;//// If there aren't enough players to start the game, wait 30 secs for players to join
			if (players.size() < GameServer.minPlayers) waitTime = 30000;////  Else, only wait 7 secs as more players aren't required
			if(Functions.waitTimeOrTrue(waitTime, playerJoined)) { // If player joined, add them to list of players. 
				Player plr = (Player) playerJoined.arg;
				System.out.println(plr.name.concat(" is being added to the list of players"));
				players.add(plr);
			}else if (players.size() >= GameServer.minPlayers) enoughPlayers = true; // break loop if no players join and we dont need more.
			playerJoined.reset();
		}
	}
	public void init() { // readies the game for starting after the players have joined.
		waitingForPlayers = false;
		playerJoinThread.interrupt();
		

		drawPile = new CardStack(players.size());
		// Would be better off putting below code in constructor of CardStack		
		for (Player p: players) {
			p.cards.addCard(new Card(CardType.Defuse));
			for (int i = 0; i < 7; i++) {
				p.cards.addCard(drawPile.cards.pop());
			}
		}
		drawPile.insertImploadingDoggos();
		
		System.out.println("Initialised game");
	}
	public void awaitAcknowledgeAll() { // waits until players have sent an acknowledge message
		// to the server. 
		for (Player p : players) {
			while (!p.acknowledged)
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			System.out.println(p.name.concat(" has acknowledged"));
		}
	}
	public void startServer() {
		ArrayList<ClientMessage> states = new ArrayList<ClientMessage>();
		for (Player p : players) {
			states.add(ClientMessage.FullGameState(getGameState(p)));
		}
		sendToPlayers(players, states);
		
		System.out.println("Starting game");
		while (Player.totalPlayers - Player.playersDead > 1) {
			Player plr = players.get(playerGo);
			if (plr.isDead) {
				playerGo = (playerGo + 1) % Player.totalPlayers;
				continue;
			}
			plr.startTurn();
			//if (!ExplodingKittensUtils.waitTimeOrTrue(25000, plr.turnEnded))                    ------ HERE ------ 
			//////////////// maybe cut this and instead start turn and wait until turn ended or time passed.
			//////////////// would be cleaner and all the shit to do with the player can be handled in the player object.
			
			if (!Functions.waitTimeOrTrue(15000, plr.cardDrawn)) { // cardDrawn is set true immediately after tryDrawCard is invoked
				System.out.println(plr.name.concat(" is being forced to draw"));
				EventSystem.tryDrawCard.invoke(plr, false); // false is to run synchronously
			}/// By default, invoking an event runs the event handler functions asynchronously on a new thread
			//// This meant that the program didn't block on line 78, so lines 83-84 caused the playerGo to 
			//// no longer be the the index of the player who is being forced to draw a card. 
			////////////////////////////
			Functions.waitTimeOrTrue(Integer.MAX_VALUE, plr.turnEnded);// make sure that turn has actually ended
			// required because if player isn't forced to draw, then their draw is handled asynchronously, meaning thread no block.
			
			plr.cardDrawn.reset(); // Forgetting this led to infinitely switching between player turns without drawing cards
			playerGo = (playerGo + 1) % Player.totalPlayers;
		}
	}
	public void awaitPlayers() {
		
	}
	public static void onPlayerDied(Object player) {
		Player plr = (Player) player;
		int plrId = plr.playerId;
		ClientMessage message = ClientMessage.PlayerDied(plrId);	
		sendToPlayers(GameServer.game.players, message);
	}
	public static void onTryDrawCard(Object player) { // called if player tells server they want to draw a card
		if (game.players.get(game.playerGo) != (Player) player) return;// or if a player is forced to draw.
		Player plr = (Player) player;
		plr.drawCard(debug);
	}
	public static void onCardNeutralised(Object Card) {
		Card card = (Card)Card;
		card.neutralised.set();
	}
	public static void onTryPlayCard(Object plrCard) { // card will be found by invoker of this event (don't forget to do it u fucking idiot)
		PlayCardArgs playerCard = (PlayCardArgs) plrCard;
		if (!playerCard.player.cards.cards.contains(playerCard.card)) return;
		playerCard.player.playCard(playerCard.card, playerCard.args);
	}
	public static void onCardDrawn(Object Player) { // tell players card drawn
		PlrCardPair playerCard = (PlrCardPair) Player;
		playerCard.player.cardDrawn.set();
		ArrayList<ClientMessage> messages = new ArrayList<ClientMessage>();
		for (Player p : GameServer.game.players) {
			int cardId = -1;
			if (p.playerId == playerCard.player.playerId) cardId = playerCard.card.id;
			messages.add(ClientMessage.CardDrawn(playerCard.player.playerId, cardId));
		}
		sendToPlayers(GameServer.game.players, messages);
	}
	public static void sendToPlayers(ArrayList<Player> plrs, ClientMessage msg) {
		// to allow multiple plrs to be sent the same message
		
		ArrayList<ClientMessage> msgs = new ArrayList<ClientMessage>();
		for (int i = 0; i < plrs.size(); i++) {
			msgs.add(msg);
		}
		sendToPlayers(plrs, msgs);
	}
	public static void sendToPlayers(ArrayList<Player> plrs, ArrayList<ClientMessage> msgs) {
		/// Function to reduce repetitiveness of code when needing to send a group of players a message
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < plrs.size(); i++) {
			Player p = plrs.get(i);
			ClientMessage msg = msgs.get(i);
			threads.add(p.userCommunicator.sendRequestMessageAsync(msg));
		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void onExplodingKittenReplaced(Object Card) {
		Card card = (Card)Card;
		card.booleanGPA.set();
	}
	public static void onPlayerConnected(Object plr) {
		Player player = (Player) plr;
		assert(player != null);
		game.playerJoined.set(player);
		player.userCommunicator.sendRequestMessage(ClientMessage.FullGameState(game.getGameState(player)));
	}
	public static void onPlayerRequestReceived(Object plrReqPair) {
		PlayerRequestPair plrRequest = (PlayerRequestPair) plrReqPair;
		Player player = plrRequest.player;
		RequestContent request = plrRequest.request;
		
		// Assume there is already an associated player with the sender of the request
		// as requests without one will have been filtered out. 
		// player has already joined the game.
		// N.B. This subroutine is already running asynchronously to the rest of the program.
		
		if (request.requestType == RequestType.DrawCard) {
			EventSystem.tryDrawCard.invokeSync(player);
		}else if (request.requestType == RequestType.PlayCard) {
			if (request.args == null || request.args.length == 0) return;
			int cardId = (int) request.args[0];
			Card card = Card.getCardById(cardId);
			
			PlayCardArgs x = new PlayCardArgs(player, card, request.args);
			EventSystem.tryPlayCard.invokeSync(x);
		}
		
	}
	public static void onTurnStarted(Object player) {
		ClientMessage message = ClientMessage.TurnStarted(((Player)player).playerId);
		sendToPlayers(GameServer.game.players,message);
	}
	public static void onTurnEnded(Object player) {
		ClientMessage message = ClientMessage.TurnEnded(((Player)player).playerId);
		sendToPlayers(GameServer.game.players,message);
	}

	public static Thread startNewThread(Runnable runnable) { // To ease the future switching towards Threadpools
		Thread t = new Thread(runnable);
		t.start();
		return t;/// Change to return the thread so it can be joined
	}
}

/// Switching to an interface and a class which implements the interface, for handling requests 
/// from clients improves modularity and maintainability of the code, reduces unnecessary branching
/// between files, and reduces the need to have a lot of events.
interface RequestProcessor{
	public default void onPlayerRequestReceived(Object plrReqPair) {
		PlayerRequestPair plrRequest = (PlayerRequestPair) plrReqPair;
		Player player = plrRequest.player;
		RequestContent request = plrRequest.request;
		int argsSupplied = 0;
		if (request.args != null) argsSupplied = request.args.length;
		
		// Assume there is already an associated player with the sender of the request
		// as requests without one will have been filtered out. 
		// player has already joined the game.
		// N.B. This subroutine is already running asynchronously to the rest of the program.
		RequestType reqType = request.requestType;
		if (reqType == RequestType.DrawCard) {
			if (player == null) System.out.println("NULL PLAYER");
			onRequestDrawCard(player);
		}else if (reqType == RequestType.PlayCard) {
			Card card = Card.getCardById((int) request.args[0]);
			onRequestPlayCard(new PlayCardArgs(player, card, request.args));
		}else if (reqType == RequestType.MessagePeers) {
			if (argsSupplied != 1) return;
			onRequestMessagePeers((HumanPlayer) player, (String) request.args[0]);
		}else if (reqType == RequestType.RequestCheatGameState) {
			onRequestCheatGameState(player);
		}else if (reqType == RequestType.RequestGameState) {
			onRequestGameState(player);
		}else if (reqType == RequestType.Acknowledge) {
			onRequestAcknowledge(player);
		}
	}
	
	public void onRequestDrawCard(Player player);
	public void onRequestPlayCard(PlayCardArgs plrCard);
	public void onRequestMessagePeers(HumanPlayer player, String messageJSON);
	public void onRequestCheatGameState(Player player);
	public void onRequestGameState(Player player);
	public void onRequestAcknowledge(Player player);
}


class RequestHandler implements RequestProcessor{
	private GameServer game;
	public RequestHandler(GameServer game) {
		this.game = game;
	}
	
	@Override
	public void onRequestDrawCard(Player player) {
		if (game.players.get(game.playerGo) != player) return;
		Player plr = (Player) player;
		plr.drawCard(GameServer.debug);
	}

	@Override
	public void onRequestPlayCard(PlayCardArgs plrCard) {
		PlayCardArgs playerCard = (PlayCardArgs) plrCard;
		if (!playerCard.player.cards.cards.contains(playerCard.card)) return;
		playerCard.player.playCard(playerCard.card, playerCard.args);
	}

	@Override
	public void onRequestMessagePeers(HumanPlayer player, String messageJSON) {
		ClientMessage message = ClientMessage.MessageFromPeers(messageJSON);
		GameServer.sendToPlayers(game.players, message);
	}

	@Override
	public void onRequestCheatGameState(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestGameState(Player player) {
		GameState state = game.getGameState(player);
		ClientMessage message = ClientMessage.FullGameState(state);
		
		player.userCommunicator.sendRequestMessage(message);
	}

	@Override
	public void onRequestAcknowledge(Player player) {
		player.acknowledged = true;
	}
}