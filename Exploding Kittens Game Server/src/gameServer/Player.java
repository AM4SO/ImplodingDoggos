package gameServer;

import java.util.ArrayList;

import gameServer.ImplodingDoggosUtils.BooleanVariable;
import gameServer.ImplodingDoggosUtils.ClientMessage;
import gameServer.ImplodingDoggosUtils.ClientMessageContent;
import gameServer.ImplodingDoggosUtils.ClientMessageType;
import gameServer.ImplodingDoggosUtils.Functions;
import gameServer.ImplodingDoggosUtils.MultiSetterBooleanVariable;

public class Player {
	static int totalPlayers = 0, playersDead = 0, idCount = 0;
	protected static ArrayList<Player> players = new ArrayList<Player>();
	static protected MultiSetterBooleanVariable PlrCreating;
	public static Player getPlayerById(int playerId) {
		for(Player p : Player.players) {
			if (playerId == p.playerId) return p;
		}
		return null;
	}
	public static Player getPlayerByUserId(long userId) {
		for (int i = 0; i < Player.players.size(); i++){
			Player p = Player.players.get(i);
			if(p.userId == userId) return p;
		}
		
		return null;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	public BooleanVariable turnEnded, cardDrawn;
	private GameServer game;
	public int playerId;
	public Hand cards;
	public String name;
	public boolean isDead;
	public PlayerCommunicator userCommunicator;
	protected long userId;
	private ArrayList<Card> cardsPlaying;
	public int turnsLeft;
	public boolean acknowledged;
	
	
	public PlayerState getPlayerState(boolean cheat) {
		PlayerState ret;
		if (cheat) ret = new CheatPlayerState();
		else ret = new PlayerState();
		
		ret.playerId = playerId;
		ret.numCards = cards.cards.size();
		ret.isDead = isDead;
		ret.turnsLeft = turnsLeft;
		ret.userId = userId;
		
		if (cheat) ((CheatPlayerState) ret).cards = cards;
		
		return ret;
	}
	
	public Player(long userId) {
		init(userId);
	}
	public Player() {
		init(GameServer.random.nextLong());
	}
	public void init(long userId) {
		Player.players.add(this);
		userCommunicator = new PlayerCommunicator(this);
		isDead = false;
		this.game = GameServer.game;
		Player.totalPlayers++;
		turnEnded = new BooleanVariable(false);
		cardDrawn = new BooleanVariable(false);
		playerId = Player.idCount++;
		name = "player ".concat(Integer.toString(playerId));
		cards = new Hand();
		cardsPlaying = new ArrayList<Card>();
		turnsLeft = 0;
		this.userId = userId;
	}
	
	public void die() {
		turnsLeft = 0;
		isDead = true;
		Player.playersDead++;
		System.out.println(name.concat(" has died'd"));
		EventSystem.playerDied.invokeSync(this);
	}
	
	public void startTurn() {
		turnsLeft = 1; 
		turnEnded.reset();
		// do some player informing stuff init.
		System.out.println(name.concat("'s turn..."));
		EventSystem.turnStarted.invoke(this);
	}
	
	public void endTurn() {
		while (cardsPlaying.size() > 0) 
		{
			Card c = cardsPlaying.remove(0); 
			cardsPlaying.remove(c);
			game.disposePile.add(c);
		}
		turnsLeft--;
		// tell client turn has ended
		
		if (turnsLeft <= 0) turnEnded.set();
		System.out.println(name.concat("'s turn has ended"));
		EventSystem.turnEnded.invoke(this);
	}
	
	public void endTurn(Void thing) {
		endTurn();
	}
	
	public void drawCard() {
		drawCard(false);
	}
	
	public void drawCard(boolean debug) {
		Card card = game.drawPile.drawCard(this);
		boolean addCard = true;
		if (addCard) cards.addCard(card);
		if(debug) System.out.println(name.concat(" has drawn ").concat(card.cardType.toString()));
		if (card.cardType == CardType.ExplodingKitten) {
			playCard(card);		
		}
		endTurn();
	}
	
	public void playCard(Card c) {playCard(c, null);}
	
	public void playCard(Card card, Object[] args) {
		// check card is in hand -- probably done by caller of this function
		// remove card from hand
		// depending on card type, either place in dispose pile, or place it in the 
		// player's playing pile
		cards.removeCard(card);
		switch (card.cardType) {
		case ExplodingKitten:
			playExplodingKitten(card);
			break;
		case Attack://////////////////////////////////////////// LEFT OFF HERE -------- Forgetn't to account for Nope
			//int playerId = (int) args[1];
			//Player plr = Player.getPlayerById(playerId);
			//game.playerGo = game.players.indexOf(plr);
			endTurn();
		default:
			break;
		}
		
		cardsPlaying.add(card);
	}
	
	public void playExplodingKitten(Card card) {
		if (!Functions.waitTimeOrTrue(5000, card.neutralised, false)) {
			System.out.println(name.concat(" has failed to defuse the exploding kitten and has died'd"));
			card.neutralised.reset();
			die();
		}else {
			// Tell client/player
			// Another wait for them to take an action, i.e. place exploding kitten in deck.
			// if no action after x seconds, place wherever
			if (!Functions.waitTimeOrTrue(5000, card.booleanGPA, false))
				game.drawPile.cards.push(card);
			System.out.println(name.concat(" has placed the exploding kitten on the top of the deck"));
			card.booleanGPA.reset();
		}
		card.neutralised.reset();
	}
}
