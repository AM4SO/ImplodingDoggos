package gameServer;

import java.util.function.Consumer;

public class Player {
	static int totalPlayers = 0, playersDead = 0;
	public BooleanVariable hasTurn;
	private GameServer game;
	public Hand cards;
	public Player(GameServer game) {
		this.game = game;
		Player.totalPlayers++;
		hasTurn = new BooleanVariable(false);//, (Consumer<Void>)this::endTurn
	}
	public void die() {
		Player.playersDead++;
	}
	public void startTurn() {
		hasTurn.set();
		// do some player informing stuff init.
	}
	public void endTurn() {
		hasTurn.set();
	}public void endTurn(Void thing) {
		hasTurn.set();
	}
	public void drawCard() {
		Card card = game.drawPile.drawCard();
		if (card.cardType == CardType.ExplodingKitten) {
			/// tell client exploding kitten gonna kill.
			/// if not defused within x seconds, die player.
			
		}
	}
}
