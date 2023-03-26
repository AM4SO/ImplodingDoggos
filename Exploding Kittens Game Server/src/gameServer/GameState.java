package gameServer;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable{
	private static final long serialVersionUID = 1L;
	public GameState() {
		cards= null;
		players = null;
		localPlayerHand = null;
		playerTurn = -1;
	}
	
	public ArrayList<CardState> cards;
	public ArrayList<PlayerState> players;
	public HandState localPlayerHand;
	public int playerTurn;
}