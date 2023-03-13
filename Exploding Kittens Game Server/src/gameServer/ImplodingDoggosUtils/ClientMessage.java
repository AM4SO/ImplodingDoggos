package gameServer.ImplodingDoggosUtils;

import java.io.Serializable;

import org.json.JSONObject;

import gameServer.GameState;
import gameServer.Player;

public class ClientMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	public int playerId;
	public ClientMessageContent cont;
	
	public static ClientMessage FullGameState(Player plr, GameState state) {
		ClientMessage ret = new ClientMessage();
		ret.playerId = plr.playerId;
		ret.cont = new ClientMessageContent(ClientMessageType.FullGameState);
		ret.cont.args = new Object[] {state};
		
		return ret;
	}
	public static ClientMessage MessageFromPeers(Player plr, JSONObject message) {
		ClientMessage ret = new ClientMessage();
		ret.playerId = plr.playerId;
		ret.cont = new ClientMessageContent(ClientMessageType.MessageFromPeers);
		ret.cont.args = new Object[] {message};
		
		return ret;
	}
}