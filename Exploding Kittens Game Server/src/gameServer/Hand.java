package gameServer;

import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable{
	private static final long serialVersionUID = 1L;
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
	public HandState getHandState() {
		HandState ret = new HandState();
		ret.cards = Card.getAllCardStates(cards);
		return ret;
	}
}