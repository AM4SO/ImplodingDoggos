package gameServer;

public class PlayerState {
	int playerId;
	int numCards;
	String name;
	boolean isDead;
	int turnsLeft;
}
class CheatPlayerState extends PlayerState{
	Hand cards;
}