package gameServer;

import java.io.Serializable;
import java.util.ArrayList;

import gameServer.ImplodingDoggosUtils.BooleanVariable;
class CardState implements Serializable{
	private static final long serialVersionUID = 1L;
	public CardPack cardPack;
	public DogCardType SpecificCardType;
	public CardType cardType;
	//public CardAssetPack assetPack
	public int id;
}
public class Card implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static int CardIdCounter = 0;
	public static ArrayList<Card> Cards = new ArrayList<Card>();;
	
	public static Card getCardById(int cardId) {
		for(Card c : Cards) {
	 		if (c.id == cardId) return c;
		}
		return null;
	}
	public CardState getCardState() {
		CardState ret = new CardState();
		ret.cardPack = cardPack;
		ret.cardType = cardType;
		ret.SpecificCardType = specificCardType;
		//ret.assetPack = assetPack;
		ret.id = id;
		
		return null;
	}
	
	public CardPack cardPack;
	public DogCardType specificCardType;
	public CardType cardType;
	public double randomNumber;
	public CardAssetPack assetPack;
	public BooleanVariable neutralised;
	public BooleanVariable booleanGPA; // general purpose attribute
	public int id;
	public Card(CardType type) {
		id = Card.CardIdCounter++;
		cardType = type;
		neutralised = new BooleanVariable(false); // neutralised: Noped or defused
		booleanGPA = new BooleanVariable(false);
		Card.Cards.add(this);
	}
	
	public void redoRandom() {
		randomNumber = GameServer.random.nextDouble();
	}
	public static ArrayList<CardState> getAllCardStates() {
		return getAllCardStates(Card.Cards);
	}public static ArrayList<CardState> getAllCardStates(ArrayList<Card> cards) {
		ArrayList<CardState> ret = new ArrayList<CardState>();
		for(Card c : cards) {
			ret.add(c.getCardState());
		}
		return ret;
	}
}
class CardAssetPack implements Serializable{ // change to import packs from file which uses JSON format. 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int idCounter=0;
	public int assetPackId;
	final static CardAssetPack[] ExplodingKittens = new CardAssetPack[] {
			new CardAssetPack("A dog ate chocolate and malfunctioned. It will soon die and you will explode.",
					"Dog")
	};
	public CardAssetPack(String cardMessage, String cardDescription, String imgPath) {
		init(cardMessage, cardDescription, imgPath);
	}
	public CardAssetPack(String cardMessage, String cardDescription) {
		init(cardMessage,cardDescription,"");
	}
	public CardAssetPack() {
		init("","","");
	}
	private void init(String cardMessage, String cardDescription, String imgPath) {
		assetPackId = idCounter++;
	}
}
enum CardPack{
	BaseGame,
	ExplodingDoggos
}
enum DogCardType{
	WatermelonDog,
	TacoDog,
	PotatoDog,
	BeardDog,
	RainbowRalphingDog
}