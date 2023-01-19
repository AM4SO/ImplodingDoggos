package gameServer;

import java.util.Random;

public class Card {
	public CardPack cardPack;
	public DogCardType specificCardType;
	public CardType cardType;
	public double randomNumber;
	public CardAssetPack assetPack;
	public BooleanVariable neutralised;
	public BooleanVariable booleanGPA; // general purpose attribute
	public Card(CardType type) {
		cardType = type;
		neutralised = new BooleanVariable(false); // neutralised: Noped or diffused
		booleanGPA = new BooleanVariable(false);
	}
	
	public void redoRandom() {
		randomNumber = GameServer.random.nextDouble();
	}
}
class CardAssetPack{ // change to import packs from file which uses JSON format. 
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