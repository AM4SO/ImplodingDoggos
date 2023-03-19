package gameServer.ImplodingDoggosUtils;

import gameServer.Card;
import gameServer.Player;

public class PlrCardPair {
	public Player player;
	public Card card;
	public PlrCardPair(Player plr, Card c) {
		this.player = plr;
		this.card = c;
	}
}