package gameServer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import gameServer.clientSide.LanRemoteGameAdapter;
import gameServer.clientSide.LanScanner;
import gameServer.clientSide.RemoteGameAdapter;
import gameServer.clientSide.RemoteGameMessageAdapter;

/* This test case will test the game maker class and its ability to create a game instance, allow it to be customised,
 * and allow it to run the game on command. 
 * This test case will be acting as the LAN game creation tab for the client app. 
 * This test case will also ensure that the lan server and scanner classes are working as intended. 
 */
class GameMakerTest {
	int port = 25566;
	
	
	@Test
	void test() {
		GameMaker gameMaker = new GameMaker(port,"testing",0,2,5,"");
		/// gameserver instance has been created and is listening for others clients to join.
		Scanner scanner = new Scanner(System.in);
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LanConnectorTestClient testClient = new LanConnectorTestClient(63432434532l, port);
		boolean chosenServer = false;
		PacketCreator serverDetails = null;
		while (!chosenServer) {
			chosenServer = true;
			testClient.findLocalServers();
			//System.out.println("Servers found: ");
			int i = 0;
			ArrayList<PacketCreator> responses = testClient.lanScanner.responses;
			System.out.println(responses.size());
			for(PacketCreator server : responses) {
				System.out.println(String.valueOf(i) + ": " + server.getGameName());
			}
			System.out.println();
			System.out.println("Which server to join? (or press enter to rescan)   ");
			try {
				i = scanner.nextInt();
				if (i >= testClient.lanScanner.responses.size()) {
					chosenServer = false;
					continue;
				}
			}catch (InputMismatchException e) {
				chosenServer = false;
				scanner.next();
				continue;
			}
			serverDetails = testClient.lanScanner.responses.get(i);
		}
		/// Server details for the chosen server to connect are stored in serverDetails.
		try {
			LanRemoteGameAdapter gameConnector = new LanRemoteGameAdapter(
					(Inet4Address)Inet4Address.getByAddress(serverDetails.ipAddress), serverDetails.port);
			gameConnector.setRemoteGameMessageAdapter(new TestClientMessageHandler(gameConnector));
			gameConnector.connectToGame();
			
			
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
		System.out.println("Attempting to start game");
		gameMaker.startGame();
		
		gameMaker.end();
		
		
	}

	
	class LanConnectorTestClient extends TestClient{
		LanScanner lanScanner;
		public LanConnectorTestClient(long userId, int port) {
			super(userId, port);
			lanScanner = new LanScanner();
		}
		
		public void findLocalServers() {
			System.out.println("LanScanner:    Scanning for local servers....");
			lanScanner.findLocalServers();
		}
		
		
	}
	class TestClientMessageHandler implements RemoteGameMessageAdapter{
		RemoteGameAdapter parent;
		ArrayList<PlayerState> players;
		public TestClientMessageHandler(RemoteGameAdapter parent) {
			this.parent = parent;
		}
		public PlayerState getPlayerByUserId() {
			return null;
		}public PlayerState getPlayerByPlayerId(int playerId) {
			for (PlayerState state : players) {
				if (state.playerId == playerId)
					return state;
			}
			return null;
		}
		
		@Override
		public void onTurnStarted(int playerId) {
			if (getPlayerByPlayerId(playerId).userId == parent.getUser().userId)
				parent.drawCard();
		}

		@Override
		public void onCardPlayed(int cardId, int playerId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGameStateReceived(GameState gameState) {
			System.out.println("GAMESTATE RECEIVED");
			players = gameState.players;
			parent.sendAcknowledge();
		}

		@Override
		public void onPlayerJoined(int playerId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPlayerDied(int playerId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCardDrawn(int cardId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCheatGameStateReceived(CheatGameState cheatGameState) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMessageFromPeers(JSONObject message) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTurnEnded(int playerId) {
			// TODO Auto-generated method stub
			
		}
		
	}
}