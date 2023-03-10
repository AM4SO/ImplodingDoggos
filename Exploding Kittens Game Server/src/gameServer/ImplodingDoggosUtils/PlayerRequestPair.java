package gameServer.ImplodingDoggosUtils;

import gameServer.Player;
import gameServer.RequestContent;

public class PlayerRequestPair{
	public Player player;
	public RequestContent request;
	public PlayerRequestPair(Player plr, RequestContent req) {
		player = plr;
		request = req;
	}
}