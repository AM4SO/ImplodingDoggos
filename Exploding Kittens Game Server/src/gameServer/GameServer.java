package gameServer;

import java.util.ArrayList;
import java.util.Random;

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
	ServerListener listener;
	public GameServer(int port) {
		GameServer.game = this;
		playerJoined = new MultiSetterBooleanVariable(false);
		EventSystem.Initialise();
		disposePile = new ArrayList<Card>();
		players = new ArrayList<Player>();
		
		// below thread will only run until waitingForPlayers is false
		//new Thread(() -> {HumanPlayer.PlayerCreationHandler();}).start();
		
		listener = new ServerListener(port);
		System.out.println("Starting listener");
		listener.start();

		System.out.println("Started listener");
	}public void init() {
		players.add(new GameTestingPlayer());
		//players.add(new GameTestingPlayer());
		
		//// Give players time to join the game
		boolean enoughPlayers = false;
		while (!enoughPlayers) { // Pause thread, tell other thread to awake this thread in x time, and tell otherer thread to
			///                     Tell this thread when plr joined. -- Thread.Notify();
			int waitTime = 6000;//// If there aren't enough players to start the game, wait 30 secs for players to join
			if (players.size() < GameServer.minPlayers) waitTime = 30000;////  Else, only wait 7 secs as more players aren't required
			if(Functions.waitTimeOrTrue(waitTime, playerJoined)) { // If player joined, add them to list of players. 
				Player plr = (Player) playerJoined.arg;
				System.out.println(plr.name.concat(" is being added to the list of players"));
				players.add(plr);
			}else if (players.size() >= GameServer.minPlayers) enoughPlayers = true; // break loop if no players join and we dont need more.
			playerJoined.reset();
		}
		
		waitingForPlayers = false;
		
		drawPile = new CardStack(players.size());
		
		for (Player p: players) {
			p.cards.addCard(new Card(CardType.Defuse));
			for (int i = 0; i < 7; i++) {
				p.cards.addCard(drawPile.cards.pop());
			}
		}
		drawPile.insertImploadingDoggos();
		
		System.out.println("Initialised game");
	}
	public void startServer() {
		while(waitingForPlayers) continue;
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
	public static void onCardDrawn(Object Player) {
		PlrCardPair playerCard = (PlrCardPair) Player;
		playerCard.player.cardDrawn.set();
	}
	public static void onExplodingKittenReplaced(Object Card) {
		Card card = (Card)Card;
		card.booleanGPA.set();
	}
	public static void onTurnEnded(Object plr) {
		Player player = (Player) plr;
		player.endTurn();
	}
	public static void onPlayerConnected(Object plr) {
		Player player = (Player) plr;
		assert(player != null);
		game.playerJoined.set(player);
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
}