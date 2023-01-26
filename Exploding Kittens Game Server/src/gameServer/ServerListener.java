package gameServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.json.*;

public class ServerListener extends Thread {
	public java.net.ServerSocket listener;
	private int port;
	private ArrayList<SocketManager> socketInputManagers;
	public ServerListener(int port) {
		socketInputManagers = new ArrayList<SocketManager>();
		this.port = port;
	}
	@Override
	public void run() {
		try {
			listener = new ServerSocket(port);
			while (GameServer.game.waitingForPlayers) {
				Socket socket = listener.accept();
				System.out.println("received connection");
				SocketManager socketManager = new SocketManager(socket);
				socketManager.start();/////////////////////////////////////////////////////// Error to write about? Use start not run
				socketInputManagers.add(socketManager);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
class SocketManager extends Thread{ // Handles all traffic on a specific socket
							// Required as multiple players may connect using the same Socket 
	                        // because of intermediary server. Makes sure that received requests
	                        // are associated with the correct players.
	//private ObjectInputStream stream;
	ObjectInputStream stream;
	private Socket socket;
	public SocketManager(Socket sock) {
		try {
			this.stream = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket = sock;
	}
	@Override
	public void run() {
		try {
			/// The first 4 bits of any request represent an integer which represents the length
			/// of the request (excluding the 4 prefix bytes)
			///byte[] prefix = new byte[4];
			///for (int i = 0; i < 4; i++) {
			///	prefix[i] = (byte) stream.read();
			///}
			///ByteBuffer x = ByteBuffer.wrap(prefix);
			///int numBytes = x.getInt();
			/// Now the rest of the request must be read 
			///byte[] requestBytes = new byte[numBytes];
			///for (int i = 0; i < numBytes; i++) {
			///	requestBytes[i] = (byte) stream.read();
			///}
			/// Next convert the request to a string and parse it using the JSON parser
			/// into a request object
			//String requestString = String.valueOf(requestBytes);
			//JSONObject obj = new JSONObject(requestString);
			//Request req = Request.fromJsonObject(obj);
			//////////////////////////////////////////////////////
			///// Just learnt that I didn't need to use JSON and build a Request object myself    FUCK
			///// I can cast my InputStream to an ObjectInputStream and cast the result to a Request
			
			// All of the above can be replaced with:   (I wasted ages trying to figure out why shitty JSON wouldn't import properly for nothing :) )
			

			try {
				Request request = (Request) stream.readObject();

				Player sender = HumanPlayer.getPlayerByUserId(request.userId);
				if (sender==null && GameServer.game.waitingForPlayers) { // plr doesn't yet exist and game is waiting for players so add player to game
					sender = new HumanPlayer(request.userId, new ObjectOutputStream(socket.getOutputStream()));
					EventSystem.playerAdded.invoke(sender);
				}
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}