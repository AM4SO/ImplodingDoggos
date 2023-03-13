package gameServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

import gameServer.ImplodingDoggosUtils.ClientMessage;

public class PlayerCommunicator extends Thread {
	public PlayerCommunicator() {
		
	}
	public void sendRawMessage(byte[] data) throws IOException { // probably useless function. 
		/////////Just exists to decompose task of communicating with the user.
	}public void sendRawMessage(String data) throws IOException {
		byte[] bytes = data.getBytes();
		sendRawMessage(bytes);
	}public void sendRequestMessage(ClientMessage message) {
		
	}
	public void sendRequestMessageAsync(ClientMessage message) {
		GameServer.startNewThread(() -> sendRequestMessage(message));
	}
}

class HumanPlayerCommunicator extends PlayerCommunicator{
	private Socket socket;
	private ObjectOutputStream sendStream;
	public HumanPlayerCommunicator(ObjectOutputStream stream) {
		super();
		sendStream = stream;
		try {
			sendStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void sendRawMessage(byte[] data) throws IOException {
		sendStream.write(data);
	}
	@Override
	public void sendRequestMessage(ClientMessage req) {
		try {
			sendStream.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}






