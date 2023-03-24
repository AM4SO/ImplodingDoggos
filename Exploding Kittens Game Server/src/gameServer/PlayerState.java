package gameServer;

import java.io.Serializable;

public class PlayerState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int playerId;
	int numCards;
	String name;
	boolean isDead;
	int turnsLeft;
	long userId;
}
class CheatPlayerState extends PlayerState implements Serializable{
	private static final long serialVersionUID = 1L;
	Hand cards;
}