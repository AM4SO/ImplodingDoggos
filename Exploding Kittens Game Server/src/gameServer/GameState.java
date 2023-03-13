package gameServer;

import java.util.ArrayList;

public class GameState {
	ArrayList<Card> cards;
	ArrayList<PlayerState> players;
	Hand localPlayerHand;
	int playerTurn;
}