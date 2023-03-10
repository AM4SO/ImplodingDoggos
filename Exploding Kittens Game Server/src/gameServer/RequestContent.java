package gameServer;

import java.io.Serializable;

public class RequestContent implements Serializable{
	private static final long serialVersionUID = 1L;
	public RequestContent() {
		
	}public RequestContent(RequestType reqType) {
		this.requestType = reqType;
	}

	public RequestType requestType;
	public Object[] args;
}