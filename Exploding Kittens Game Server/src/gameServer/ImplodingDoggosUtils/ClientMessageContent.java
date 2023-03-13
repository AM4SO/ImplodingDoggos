package gameServer.ImplodingDoggosUtils;

import java.io.Serializable;

public class ClientMessageContent implements Serializable{
	public ClientMessageContent(ClientMessageType messageType) {
		this.messageType = messageType;
	}
	public ClientMessageContent() {
		// TODO Auto-generated constructor stub
	}
	private static final long serialVersionUID = 1L;
	public ClientMessageType messageType;
	public Object[] args;
}