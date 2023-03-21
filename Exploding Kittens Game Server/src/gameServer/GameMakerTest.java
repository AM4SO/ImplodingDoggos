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

import org.junit.jupiter.api.Test;

import gameServer.clientSide.LanRemoteGameAdapter;
import gameServer.clientSide.LanScanner;

/* This test case will test the game maker class and its ability to create a game instance, allow it to be customised,
 * and allow it to run the game on command. 
 * This test case will be acting as the LAN game creation tab for the client app. 
 * This test case will also ensure that the lan server and scanner classes are working as intended. 
 */
class GameMakerTest {
	int port = 25565;
	@Test
	void test() {
		GameMaker gameMaker = new GameMaker(port,"testing",0,2,5,"");
		GameMaker gameMaker2 = new GameMaker(port+1,"testing2",0,2,5,"");
		/// gameserver instance has been created and is listening for others clients to join.
		
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
			System.out.println("Servers found: ");
			int i = 0;
			for(PacketCreator server : testClient.lanScanner.responses) {
				System.out.println(String.valueOf(i) + ": " + server.getGameName());
			}
			System.out.println("Which server to join? (or press enter to rescan)   ");
			String selection = "";
			
			try {
				selection = String.valueOf(System.in.read());
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				i = Integer.parseInt(selection);
			}catch(NumberFormatException e) {
				chosenServer = false;
				continue;
			}
			if (i >= testClient.lanScanner.responses.size())
			{
				chosenServer = false;
				continue;
			}
			serverDetails = testClient.lanScanner.responses.get(i);
		}
		/// Server details for the chosen server to connect are stored in serverDetails.
		try {
			LanRemoteGameAdapter gameConnector = new LanRemoteGameAdapter(
					(Inet4Address)Inet4Address.getByAddress(serverDetails.ipAddress), serverDetails.port);
			gameConnector.connectToGame();
			
			
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}

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
}