package gameServer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.Serializable;

import org.json.JSONObject;

public class Request implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int gameId; // This program will never use this. -- Ignored
	public long userId; // Used to differentiate between players when one connection is used for multiple players. 
	public long sessionToken;// This program will never use this. -- Ignored
	public RequestContent content; // main part of request - includes game shit. 

	
	////////////////////////////////////////////////////////// JOIN GAME REQUEST
	public static Request JoinGameRequest(long userId) {
		Request ret = new Request();
		ret.userId = userId;
		ret.content = new RequestContent(RequestType.JoinGame);
		return ret;
	}
	public static Request JoinGameRequest(long userId, int gameId, long sessionToken) {
		Request ret = new Request();
		ret.userId = userId;
		ret.gameId = gameId;
		ret.sessionToken = sessionToken;
		ret.content = new RequestContent(RequestType.JoinGame);
		return ret;
	}
	/////////////////////////////////////////////////////////////////////////////////
	public static Request DrawCardRequest(long userId) {
		Request ret = new Request();
		ret.userId = userId;
		ret.content = new RequestContent(RequestType.DrawCard);
		return ret;
	}
	public static Request PlayCardRequest(long userId, int cardId, Object[] args) {
		Request ret = new Request();
		ret.userId = userId;
		ret.content = new RequestContent(RequestType.PlayCard);
		return ret;
	}
	public static Request MessagePeersRequest(long userId, String message) {
		Request ret = new Request();
		ret.userId = userId;
		ret.content = new RequestContent(RequestType.MessagePeers);
		ret.content.args = new Object[] {message};
		return ret;
	}
	public static Request GameStateRequest() {
		System.err.write(0);
		return null;
	}
	public static Request Acknowledge(long userId) {
		Request ret = new Request();
		ret.userId = userId;
		ret.content = new RequestContent(RequestType.Acknowledge);
		return ret;
	}
	
}