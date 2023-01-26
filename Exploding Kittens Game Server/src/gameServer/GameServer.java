package gameServer;

import java.util.ArrayList;
import java.util.Random;

public class GameServer {
	static final boolean debug = true;
	static final int minPlayers = 2;
	
	public java.util.concurrent.Executor thread;
	static GameServer game;
	public static Random random = new Random();
	public ArrayList<Player> players;
	public CardStack drawPile;
	public int playerGo = 0;
	public ArrayList<Card> disposePile;
	public boolean waitingForPlayers = true;
	public BooleanVariable playerJoined;
	ServerListener listener;
	public GameServer(int port) {
		GameServer.game = this;
		playerJoined = new BooleanVariable(false);
		EventSystem.Initialise();
		disposePile = new ArrayList<Card>();
		players = new ArrayList<Player>();
		
		listener = new ServerListener(port);
		System.out.println("Starting listener");
		listener.start();

		System.out.println("Started listener");
	}public void init() {
		players.add(new GameTestingPlayer());
		players.add(new GameTestingPlayer());
		players.add(new GameTestingPlayer());
		
		boolean enoughPlayers = false;
		while (!enoughPlayers) {
			int waitTime = 70000;
			if (players.size() < GameServer.minPlayers) waitTime = 30000;
			boolean newJoined = ExplodingKittensUtils.waitTimeOrTrue(30000, playerJoined);
			Player plr = (Player) playerJoined.arg;
			players.add(plr);
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
			if (!ExplodingKittensUtils.waitTimeOrTrue(5000, plr.cardDrawn)) { 
				System.out.println(plr.name.concat(" is being forced to draw"));
				EventSystem.tryDrawCard.invoke(plr);
				//plr.drawCard();
			}
			////////////////////////////
			plr.cardDrawn.reset(); // Forgetting this led to infinitely switching 
			plr.endTurn(); ///        between player turns without drawing cards
			playerGo = (playerGo + 1) % Player.totalPlayers;
		}
	}
	public void awaitPlayers() {
		
	}
	public static void onCardNeutralised(Object Card) {
		Card card = (Card)Card;
		card.neutralised.set();
	}
	public static void onTryDrawCard(Object player) { // called if player tells server they want to draw a card
		if (game.players.get(game.playerGo) != (Player) player) return;// or if a player is forced to draw.
		Player plr = (Player) player;
		plr.drawCard(debug);
	}
	public static void onTryPlayCard(Object plrCard) { // card will be found by invoker of this event (don't forget to do it u fucking idiot)
		PlrCardPair playerCard = (PlrCardPair) plrCard;
		playerCard.player.playCard(playerCard.card);
	}
	public static void onCardDrawn(Object Player) {
		PlrCardPair playerCard = (PlrCardPair) Player;
		//player.cardDrawn.set();
		//player.drawCard(true);
		
		//if(playerCard.card.cardType != CardType.ExplodingKitten)
		//	playerCard.player.cardDrawn(playerCard.card);
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
	public static void onCardPlayed(Object plrCardPair) {
		PlrCardPair plrCard = (PlrCardPair) plrCardPair;
		plrCard.player.playCard(plrCard.card.id);
	}
	public static void onPlayerAdded(Object plr) {
		Player player = (Player) plr;
		game.playerJoined.set(player);
	}
}
// should the way the program works be different to what i say it is in the design.