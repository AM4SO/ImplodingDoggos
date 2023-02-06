package gameServer;
/// userId matters
/// sessionToken and gameId don't

import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	public TestClient(long userId, int port) {
		this.userId = userId;
		this.port = port;
	}
	@Override
	public void run() {
		
		try {
			System.out.println("RequestTypes: ");
			for (RequestType rt : RequestType.values()) {
				System.out.println(rt);
			}
			connector = new Socket(Inet4Address.getByName("localhost"), port);
			outStream = new ObjectOutputStream(connector.getOutputStream());
			outStream.flush();
			Request r = new Request();
			r.userId = userId;
			r.content = new RequestContent();
			r.content.requestType = RequestType.JoinGame;
			outStream.writeObject(r);
			
			inStream = new ObjectInputStream(connector.getInputStream());// blocks off until outputStream flushed on other end
			new Thread(this::messageHandler).start();
			
			while (true) {
				try {
					//System.out.println("Enter request type: ");
					String reqType = reader.readLine();
					Request req = new Request();
					req.userId = userId;
					req.content = new RequestContent();
					req.content.requestType = RequestType.valueOf(reqType);
				
					outStream.writeObject(req);
				}catch(IllegalArgumentException e) {
					System.out.println("Bad request");
					//e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void messageHandler() {
		while (true) {
			try {
				ClientMessage message = (ClientMessage) inStream.readObject();
				//////////////////////////////////////////////////////////// Reminder: make better debug log system for less repetitive code
				if (message.cont.messageType == ClientMessageType.TurnStarted) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Request r = new Request();
					r.content = new RequestContent();
					r.userId = userId;
					r.content.requestType = RequestType.DrawCard;
					outStream.writeObject(r);
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
