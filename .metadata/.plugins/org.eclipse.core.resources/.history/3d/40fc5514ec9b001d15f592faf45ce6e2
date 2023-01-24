package gameServer;

import java.net.Socket;
import java.util.function.Consumer;

public class Player {
	static int totalPlayers = 0, playersDead = 0, idCount = 0;
	//public BooleanVariable hasTurn;
	public BooleanVariable turnEnded, cardDrawn;
	private GameServer game;
	public int playerId;
	public Hand cards;
	public String name;
	public Player(GameServer game) {
		this.game = game;
		Player.totalPlayers++;
		turnEnded = new BooleanVariable(false);
		cardDrawn = new BooleanVariable(false);
		playerId = Player.idCount++;
		name = "player ".concat(Integer.toString(playerId));
		cards = new Hand();
	}
	public void die() {
		Player.playersDead++;
	}
	public void startTurn() {
		turnEnded.reset();
		// do some player informing stuff init.
	}
	public void endTurn() {
		turnEnded.set();
	}public void endTurn(Void thing) {
		endTurn();
	}
	public void drawCard() {
		drawCard(false);
	}
	public void drawCard(boolean debug) {
		Card card = game.drawPile.drawCard(this);
		if(debug) System.out.println(name.concat(" has drawn ").concat(card.cardType.toString()));
		// tell client what card drawn
		if (card.cardType == CardType.ExplodingKitten) {
			/// tell client exploding kitten gonna kill.
			/// if not diffused within x seconds, die player.
			
			
			if (!ExplodingKittensUtils.waitTimeOrTrue(5000, card.neutralised, false)) {
				if (debug) System.out.println(name.concat(" has failed to diffuse the exploding kitten and has died'd"));
				card.neutralised.reset();
				die();
			}else {
				// Tell client/player
				// Another wait for them to take an action, i.e. place exploding kitten in deck.
				if (!ExplodingKittensUtils.waitTimeOrTrue(5000, card.booleanGPA, false))
					game.drawPile.cards.push(card);
				if (debug) System.out.println(name.concat(" has placed the exploding kitten on the top of the deck"));
				// if no action after x seconds, place wherever
				//if (!card.booleanGPA.value) game.drawPile.cards.push(card);
				card.booleanGPA.reset();
				
			}
			card.neutralised.reset();
		}
	}
}
