package gameServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

public class PlayerCommunicator extends Thread {
	public PlayerCommunicator() {
		
	}
	public void sendRawMessage(byte[] data) throws IOException { // probably useless function. 
		/////////Just exists to decompose task of communicating with the user.
	}public void sendRawMessage(String data) throws IOException {
		byte[] bytes = data.getBytes();
		sendRawMessage(bytes);
	}public void sendRequestMessage(Request req) {
		
	}
}

class HumanPlayerCommunicator extends PlayerCommunicator{
	private Socket socket;
	private ObjectOutputStream sendStream;
	public HumanPlayerCommunicator(ObjectOutputStream stream) {
		super();
		sendStream = stream;
	}
	@Override
	public void sendRawMessage(byte[] data) throws IOException {
		sendStream.write(data);
	}
	@Override
	public void sendRequestMessage(Request req) {
		try {
			sendStream.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}






class Request implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int gameId; // This program will never use this. -- Ignored
	public long userId; // Used to differentiate between players when one connection is used for multiple players. 
	public long sessionToken;// This program will never use this. -- Ignored
	public RequestContent content; // main part of request - includes game shit. 
	
}
class RequestContent implements Serializable{
	private static final long serialVersionUID = 1L;

	public RequestType requestType;
	public Object[] args;
}
enum RequestType{
	JoinGame,
	PlayCard,
	DrawCard,
	
}



