package gameServer.clientSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Random;

import org.json.JSONObject;

import gameServer.Request;
import gameServer.ImplodingDoggosUtils.ClientMessage;
import gameServer.ImplodingDoggosUtils.ClientMessageContent;


interface RemoteGameMessageAdapter{
	public void onTurnStarted(int playerId);
	public void onCardPlayed(int cardId, int playerId);
	public void onGameStateReceived();
	public void onPlayerJoined(int playerId);
	public void onPlayerDied(int playerId);
	public void onCardDrawn(int cardId);
	public void onCheatGameStateReceived();
	public void onMessageFromPeers(JSONObject message);
}
///////////////////////////////////////////////////////////////////////// REMINDER: SWITCH TO THREADPOOLS for reuse of threads.
public class RemoteGameAdapter{
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
	
	
	private IOResult readRequests() {
		try {
			ClientMessage request = (ClientMessage) inStream.readObject();
			ClientMessageContent cont = request.cont;
			switch (cont.messageType) {
			case TurnStarted:
				messageHandler.onTurnStarted((int)cont.args[0]);
				break;
			case CardPlayed:
				messageHandler.onCardPlayed((int)cont.args[0], (int) cont.args[1]);
			default:
				break;
			}
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			if (e.getClass() == IOException.class)
				return new IOResult((IOException)e);
		}
		return IOResult.Success;
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
}