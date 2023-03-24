package gameServer.ImplodingDoggosUtils;

import java.io.Serializable;

import org.json.JSONObject;

import gameServer.CheatGameState;
import gameServer.GameState;

public class ClientMessageContent implements Serializable{
	public ClientMessageContent(ClientMessageType messageType) {
		this.messageType = messageType;
		args = null;
	}
	public ClientMessageContent() {
		args = null;
	}
	private static final long serialVersionUID = 1L;
	public ClientMessageType messageType;
	//public int[] args;
	public Object[] args;
}