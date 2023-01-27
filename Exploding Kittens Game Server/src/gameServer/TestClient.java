package gameServer;
/// userId matters
/// sessionToken and gameId don't

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
			while (true) {
				try {
					System.out.println("Enter request type: ");
					String reqType = reader.readLine();
					Request req = new Request();
					req.userId = userId;
					req.content = new RequestContent();
					req.content.requestType = RequestType.valueOf(reqType);
				
					outStream.writeObject(req);
				}catch(IllegalArgumentException e) {
					System.out.println("Bad request");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
