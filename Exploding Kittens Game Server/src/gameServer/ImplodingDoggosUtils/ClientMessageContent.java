package gameServer.ImplodingDoggosUtils;

import java.io.Serializable;

public class ClientMessageContent implements Serializable{
	private static final long serialVersionUID = 1L;
	public ClientMessageType messageType;
	public Object[] args;
}