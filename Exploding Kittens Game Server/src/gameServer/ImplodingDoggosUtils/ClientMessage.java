package gameServer.ImplodingDoggosUtils;

import java.io.Serializable;

import org.json.JSONObject;

import gameServer.GameState;
import gameServer.Player;

public class ClientMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	public int playerId;
	public ClientMessageContent cont;
	
	public static ClientMessage FullGameState(GameState state) {
		ClientMessage ret = new ClientMessage();
		ret.cont = new ClientMessageContent(ClientMessageType.FullGameState);
		ret.cont.args = new Object[] {state};
		
		return ret;
	}
	public static ClientMessage MessageFromPeers(JSONObject message) {
		ClientMessage ret = new ClientMessage();
		ret.cont = new ClientMessageContent(ClientMessageType.MessageFromPeers);
		ret.cont.args = new Object[] {message};
		
		return ret;
	}
	public static ClientMessage PlayerDied(int playerDied) {
		ClientMessage ret = new ClientMessage();
		ret.cont = new ClientMessageContent(ClientMessageType.PlayerDied);
		ret.cont.args = new Object[] {playerDied};
		return ret;
	}
	public static ClientMessage TurnStarted(int playerTurn) {
		ClientMessage message = new ClientMessage();
		message.cont = new ClientMessageContent();
		message.cont.messageType = ClientMessageType.TurnStarted;
		message.cont.args = new Object[] {playerTurn};
		return message;
	}
	public static ClientMessage TurnEnded(int player) {
		ClientMessage message = new ClientMessage();
		message.cont = new ClientMessageContent();
		message.cont.messageType = ClientMessageType.TurnEnded;
		message.cont.args = new Object[] {player};
		return message;
	}
	public static ClientMessage CardDrawn(int player, int cardId) {
		ClientMessage message = new ClientMessage();
		message.cont = new ClientMessageContent();
		message.cont.messageType = ClientMessageType.CardDrawn;
		message.cont.args = new Object[] {player, cardId};
		return message;
	}
}