package gameServer;
/// userId matters
/// sessionToken and gameId don't

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Socket;

public class TestClient extends Thread {
	long userId;
	Socket connector;
	ObjectOutputStream outStream;
	ObjectInputStream inStream;
	int port;
	public TestClient(long userId, int port) {
		this.userId = userId;
		this.port = port;
	}
	@Override
	public void run() {
		
		
		try {
			connector = new Socket(Inet4Address.getByName("localhost"), port);
			outStream = new ObjectOutputStream(connector.getOutputStream());
			outStream.flush();
			Request req = new Request();
			req.userId = userId;
			req.content = new RequestContent();
			req.content.requestType = RequestType.JoinGame;
			
			outStream.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
