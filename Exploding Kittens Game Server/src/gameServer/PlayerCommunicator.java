package gameServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

import gameServer.ImplodingDoggosUtils.ClientMessage;
import gameServer.ImplodingDoggosUtils.ClientMessageType;

public class PlayerCommunicator extends Thread {
	int playerId;
	public PlayerCommunicator(Player parent) {
		playerId = parent.playerId;
	}
	public void sendRawMessage(byte[] data) throws IOException { // probably useless function. 
		/////////Just exists to decompose task of communicating with the user.
	}public void sendRawMessage(String data) throws IOException {
		byte[] bytes = data.getBytes();
		sendRawMessage(bytes);
	}public synchronized void sendRequestMessage(ClientMessage message) {
		message.playerId = playerId;
	}
	public Thread sendRequestMessageAsync(ClientMessage message) { // PlayerCommunicator.java
		Thread t = GameServer.startNewThread(() -> sendRequestMessage(message));
		return t; // change to return thread so caller can join thread
	}
}

class HumanPlayerCommunicator extends PlayerCommunicator{
	private Socket socket;
	private ObjectOutputStream sendStream;
	public HumanPlayerCommunicator(Player parent, ObjectOutputStream stream) {
		super(parent);
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
		sendStream.flush();
	}
	@Override
	public synchronized void sendRequestMessage(ClientMessage req) { // PlayerCommunicator.Java
		/// Came to the realisation that since some messages are sent as responses to requests from clients, 
		/// and clients messages can be received at the same time, my previous solution to stop multiple messages being
		/// sent at the same time was incomplete. A simpler solution is to just make this function synchronised, so that 
		/// callers will have to take turns in running this function. 
		/// However, my previous solution is still necessary to be used along side this, so that the thread does block off
		/// until all players have received the message, so more messages aren't sent to some players before others have received,
		/// a previous one.
		super.sendRequestMessage(req);

		try {
			sendStream.writeObject(req);
			sendStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}






