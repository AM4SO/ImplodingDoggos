package gameServer;

import java.io.IOException;
import java.net.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class GameServer {
	public java.util.concurrent.Executor thread;
	static GameServer game;
	public static Random random = new Random();
	public ArrayList<Player> players;
	public CardStack drawPile;
	public int playerGo = 0;
	public ArrayList<Card> disposePile;
	public GameServer(int port) {
		GameServer.game = this;
		EventSystem.Initialise();
		disposePile = new ArrayList<Card>();
		players = new ArrayList<Player>();
		players.add(new GameTestingPlayer(this));
		players.add(new GameTestingPlayer(this));


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
		System.out.println("Starting game");
		while (Player.totalPlayers - Player.playersDead > 1) {
			Player plr = players.get(playerGo);
			plr.startTurn();
			if (!ExplodingKittensUtils.waitTimeOrTrue(5000, plr.cardDrawn)) {
				System.out.println(plr.name.concat(" is being forced to draw"));
				plr.drawCard();
			}
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
	public static void onCardDrawn(Object plrCardPair) {
		PlrCardPair playerCard = (PlrCardPair) plrCardPair;
		if(playerCard.card.cardType != CardType.ExplodingKitten)
			game.disposePile.add(playerCard.card);
		playerCard.player.cardDrawn.set();
	}
	public static void onExplodingKittenReplaced(Object Card) {
		Card card = (Card)Card;
		card.booleanGPA.set();
	}
	public static void onTurnEnded(Object plr) {
		Player player = (Player) plr;
		player.turnEnded.set();
	}
	
}
// should the way the program works be different to what i say it is in the design.