package gameServer.ImplodingDoggosUtils;

import java.io.Serializable;

public class ClientMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	public int playerId;
	public ClientMessageContent cont;
}