package gameServer;

public class GameTestingPlayer extends Player {
	public GameTestingPlayer() {
		super();
	}
	@Override
	public void startTurn() {
		super.startTurn();
	}
	@Override
	public void endTurn() {
		super.endTurn();
	}
	@Override
	public void die() {
		super.die();
	}
	@Override
	public void drawCard() {
		super.drawCard(true);
	}
}
