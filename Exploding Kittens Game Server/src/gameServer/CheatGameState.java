package gameServer;

import java.util.ArrayList;

public class CheatGameState extends GameState {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CardStack drawPile;
	CardStack disposePile;
	ArrayList<CheatPlayerState> players;
}