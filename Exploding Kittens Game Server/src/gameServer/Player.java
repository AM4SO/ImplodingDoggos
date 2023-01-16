package gameServer;

import java.util.function.Consumer;

public class Player {
	static int totalPlayers = 0, playersDead = 0;
	//public BooleanVariable hasTurn;
	public BooleanVariable turnEnded;
	private GameServer game;
	public Hand cards;
	public Player(GameServer game) {
		this.game = game;
		Player.totalPlayers++;
		turnEnded = new BooleanVariable(false);//, (Consumer<Void>)this::endTurn
	}
	public void die() {
		Player.playersDead++;
	}
	public void startTurn() {
		turnEnded.set();
		// do some player informing stuff init.
	}
	public void endTurn() {
		turnEnded.set();
	}public void endTurn(Void thing) {
		turnEnded.set();
	}
	public void drawCard() {
		Card card = game.drawPile.drawCard();
		// tell client what card drawn
		if (card.cardType == CardType.ExplodingKitten) {
			/// tell client exploding kitten gonna kill.
			/// if not diffused within x seconds, die player.
			ExplodingKittensUtils.waitTimeOrTrue(5000, card.neutralised, false);
			
			if (!card.neutralised.value) {
				die();
				game.disposePile.add(card);
			}else {
				// Tell client/player
				// Another wait for them to take an action, i.e. place exploding kitten in deck.
				if (!ExplodingKittensUtils.waitTimeOrTrue(5000, card.booleanGPA, false))
					game.drawPile.cards.push(card);
				// if no action after x seconds, place wherever
				//if (!card.booleanGPA.value) game.drawPile.cards.push(card);
				card.booleanGPA.reset();
			}
		}
	}
}
