package gameServer;

public class GameTestingPlayer extends Player {
	public GameTestingPlayer(GameServer game) {
		super(game);
	}
	@Override
	public void startTurn() {
		System.out.println(name.concat("'s turn..."));
		super.startTurn();
	}
	@Override
	public void endTurn() {
		super.endTurn();
		System.out.println(name.concat("'s turn has eneded"));
	}
	@Override
	public void die() {
		super.die();
		System.out.println(name.concat(" has died'd"));
	}
	@Override
	public void drawCard() {
		super.drawCard(true);
	}
}
