package gameServer;

import java.io.Serializable;

public class PlayerState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int playerId;
	public int numCards;
	public boolean isDead;
	public int turnsLeft;
	public long userId;
}
class CheatPlayerState extends PlayerState implements Serializable{
	private static final long serialVersionUID = 1L;
	Hand cards;
}