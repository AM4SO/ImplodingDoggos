package gameServer.clientSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Random;

import gameServer.Request;
import gameServer.ImplodingDoggosUtils.ClientMessage;
import gameServer.ImplodingDoggosUtils.ClientMessageContent;
import gameServer.ImplodingDoggosUtils.ClientMessageType;


///////////////////////////////////////////////////////////////////////// REMINDER: SWITCH TO THREADPOOLS for reuse of threads.
public class RemoteGameAdapter implements RequestMaker{
	static Random Random = new Random();
	
	private Socket connection;
	
	///private Inet4Address ip;
	///private int port;
	///private int gameId;	   Same situation as below. Reduce number of variables, improving readability
	RemoteGameDetails game; // but sacrificing line lengths for references of information.
	
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private ImplodingDoggosUser user;
	private RemoteGameMessageAdapter messageHandler;
	
	public ImplodingDoggosUser getUser() {return user;}
	
	private IOResult readRequests() {
		while (true) {
			try {
				ClientMessage request = (ClientMessage) inStream.readObject();
				ClientMessageContent cont = request.cont;
				messageHandler.onMessageReceived(cont);
				
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				if (e.getClass() == IOException.class)
					return new IOResult((IOException)e);
			}
		}
	}
	
	public void playCard(int cardId) {
		
	}
	
	
	///public RemoteGameAdapter(Inet4Address ip, int port, int gameId, long userId, long sessionId) {
	///				Find myself having many parameters which are very closely related, which makes my functions and constructors
	///             long and difficult to read. From now on, start grouping related information together in objects.
	/// IMPORTANT:  The more grouping together I do, the more extra work has to be done by the caller of the function. 
	public RemoteGameAdapter(Inet4Address ip, int port, int gameId, ImplodingDoggosUser user) {
		init(new RemoteGameDetails(ip,port,gameId), user);
	}
	public RemoteGameAdapter(RemoteGameDetails gameToConnect, ImplodingDoggosUser user) {
		init(gameToConnect, user);
	}
	private void init(RemoteGameDetails gameToConnect, ImplodingDoggosUser user) {
		connection = new Socket();
		this.game = gameToConnect;
		this.user = user;
	}
	public void setRemoteGameMessageAdapter(RemoteGameMessageAdapter adapter) {
		this.messageHandler = adapter;
	}
	public IOResult connectToGame() {
		try {
			////         -- grouping related information can lead to better readability as shown:
		 // connection.connect(new InetSocketAddress(ip, port));     changed to:
			connection.connect(game.getSocketAddress());
			outStream = new ObjectOutputStream(connection.getOutputStream());
			outStream.flush();
			Request req = Request.JoinGameRequest(user.userId, game.gameId, user.sessionToken);
			outStream.writeObject(req);
			
			inStream = new ObjectInputStream(connection.getInputStream());
			new Thread(this::readRequests).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new IOResult(e);
		}
		return IOResult.Success;
	}

	@Override
	public void drawCard() {
		try {
			outStream.writeObject(Request.DrawCardRequest(user.userId));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messagePeers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RequestGameState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendAcknowledge() {
		try {
			System.out.println("WRITING ACKNOWLEDGE");
			outStream.writeObject(Request.Acknowledge(user.userId));
			System.out.println("WROTE ACKNOWLEDGE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
/*
 * 	JoinGame,
	PlayCard,
	DrawCard,
	MessagePeers,
	RequestGameState,
	RequestCheatGameState,*/
interface RequestMaker{
	public void playCard(int cardId);
	public void drawCard();
	public void messagePeers();
	public void RequestGameState();
	public void sendAcknowledge();
}