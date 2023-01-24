package gameServer;

import java.util.ArrayList;

public class Hand {
	public ArrayList<Card> cards;
	public Hand() {
		cards = new ArrayList<Card>();
	}
	public void addCard(Card card) {
		cards.add(card);
	}
	public void disposeCard(Card plrCard) {
		// TODO Auto-generated method stub
		GameServer.game.disposePile.add(cards.remove(cards.indexOf(plrCard)));
	}
	public void removeCard(Card card) {
		cards.remove(cards.indexOf(card));
	}
}
