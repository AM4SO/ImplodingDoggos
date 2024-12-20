package gameServer;

import java.io.Serializable;
import java.util.Stack;

import gameServer.ImplodingDoggosUtils.PlrCardPair;

public class CardStack implements Serializable{
	private static final long serialVersionUID = 1L;
	Stack<Card> cards;
	public int numPlayers;
	public CardStack(int numPlayers) { // 42 cards in one deck, excluding exploding kittens and defuse
		this.numPlayers = numPlayers;
		cards = new Stack<Card>();
		
		int numDecks = (int) Math.ceil(((double)(numPlayers))/5);
		CardType[] cardTypes = new CardType[] {
				CardType.Attack, CardType.Favour, CardType.Nope,
				CardType.Shuffle, CardType.Skip, CardType.SeeTheFuture,
		};
		int[] cardAmounts = new int[] {
				2 * numDecks, 4 * numDecks, 5 * numDecks,
				4 * numDecks, 5 * numDecks, 4 * numDecks
		};
		for (int i = 0; i < cardTypes.length; i++) {
			for (int j = 0; j < cardAmounts[i]; j++) {
				cards.push(new Card(cardTypes[i]));	
			}
		}
		DogCardType[] dogCardTypes = new DogCardType[] {
				DogCardType.BeardDog, DogCardType.PotatoDog, DogCardType.RainbowRalphingDog,
				DogCardType.TacoDog, DogCardType.WatermelonDog
		};
		for (DogCardType t : dogCardTypes) {
			for(int i = 0 ; i < 4 * numDecks; i++) {
				Card c = new Card(CardType.CatCard);
				c.specificCardType = t;
				cards.push(c);
			}		
		}
		for (int i = 0; i < (6 * numDecks) - numPlayers; i++) {
			cards.push(new Card(CardType.Defuse));
		}
		insertionRandomiseDeck();
	}
	public void insertImploadingDoggos() {
		for (int i = 0; i < numPlayers-1; i++) {
			cards.push(new Card(CardType.ExplodingKitten));
		}
		insertionRandomiseDeck();
	}
	
	public void insertionRandomiseDeck() {
		for (int i = 0; i < cards.size(); i++) {
			cards.get(i).redoRandom();
		}
		Stack<Card> randomised = new Stack<Card>();
		Card c = cards.pop();
		randomised.push(c);
		while (cards.size() > 0) {
			c = cards.pop();
			for (int i = 0; i < randomised.size(); i++) {
				if (c.randomNumber < randomised.get(i).randomNumber) {
					randomised.insertElementAt(c, i);
					break;
				}else if (i == randomised.size()-1) { // add "-1"
					randomised.insertElementAt(c, i+1);
					break; /// Add break, as insertion of element causes randomised.size() to increase,
				}///////////// resulting in infinite loop.
			}
		}
		cards = randomised;
	}
	
	public Card drawCard(Player plr) {
		Card c = cards.pop();
		EventSystem.cardDrawn.invokeSync(new PlrCardPair(plr, c));// same problem as previous here: should be synchronous
		//GameServer.onCardDrawn(new PlrCardPair(plr, c));
		return c;
	}
}
