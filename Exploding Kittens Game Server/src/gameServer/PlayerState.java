package gameServer;

public class PlayerState {
	int playerId;
	int numCards;
	String name;
	boolean isDead;
	int turnsLeft;
	long userId;
}
class CheatPlayerState extends PlayerState{
	Hand cards;
}