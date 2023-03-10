package gameServer.ImplodingDoggosUtils;

import gameServer.Card;
import gameServer.Player;

public class PlayCardArgs{
	public Player player;
	public Card card;
	public Object[] args;
	public PlayCardArgs(Player plr, Card c, Object[] arg) {
		player = plr;
		card = c;
		args = arg;
	}
}