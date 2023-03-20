package gameServer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

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
		testClient.findLocalServers();
		System.out.println("Servers found: ");
		for(PacketCreator server : testClient.lanScanner.responses) {
			System.out.println(server.getGameName());
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

class LanScanner{
	private final static String MulticastGroupIp = LanDiscoveryServer.MulticastGroupIP;
	private static final int MulticastGroupPort = LanDiscoveryServer.MulticastGroupPort;

	DatagramSocket discovererSocket;
	boolean receiving;
	ArrayList<PacketCreator> responses;
	
	public LanScanner() {
		receiving = false;
		try {
			discovererSocket = new DatagramSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void findLocalServers() {
		byte[] buff = new byte[1024];
		buff[0] = 1; // to show the server that the request is from a client asking for server details
		try {
			DatagramPacket discoveryPacket = new DatagramPacket(buff,1024, InetAddress.getByName(MulticastGroupIp), MulticastGroupPort);
			discovererSocket.send(discoveryPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		receiving = true;
		Thread t = new Thread(() -> {receiveResponses();});
		t.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		receiving = false;
	}
	public void receiveResponses() {
		responses = new ArrayList<PacketCreator>();
		while (receiving) {
			byte[] buff = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buff, 1024);
			try {
				discovererSocket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			PacketCreator response = PacketCreator.fromPacket(packet);
			//System.out.println("Lan scanner:                   Received response:");
			//System.out.println(response.toString());
			responses.add(response);
		}
	}
}