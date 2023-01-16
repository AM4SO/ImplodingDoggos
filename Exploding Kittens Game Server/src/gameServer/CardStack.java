package gameServer;

import java.util.Stack;

public class CardStack {
	Stack<Card> cards;
	
	public CardStack() {
		cards = new Stack<Card>();
		for (int i = 0; i < 4; i++) {
			cards.add(new Card(CardType.ExplodingKitten));
		}
		// TODO Auto-generated constructor stub
	}
	public Card drawCard() {
		return cards.pop();
	}

}