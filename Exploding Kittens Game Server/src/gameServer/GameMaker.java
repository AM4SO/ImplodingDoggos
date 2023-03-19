package gameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class GameMaker {
	GameServer game;
	LanDiscoveryServer lanDiscoveryServer;
	public GameMaker(int port, String gameName, int expansionPack, int numberOfBots, int maxPlayers, String joinPassword) {
		game = new GameServer(port);
		lanDiscoveryServer = new LanDiscoveryServer(port, gameName, expansionPack, numberOfBots, maxPlayers, joinPassword, game);
		GameServer.startNewThread(() -> {lanDiscoveryServer.start();});
	}
}

class LanDiscoveryServer{/// packet size = 1024
	static final String MulticastGroupIP = "234.163.014.111";
	static final int MulticastGroupPort = 25565;
	
	boolean fromClient;
	String joinPassword;
	String ipAddress;
	int port;
	int expansionPack;
	int numberOfBots;
	int maxPlayers;
	String gameName;
	MulticastSocket socket;
	PacketCreator packetHandler;
	GameServer game;
	public LanDiscoveryServer(int port, String gameName, int expansionPack, int numberOfBots, int maxPlayers, String joinPassword, GameServer game) {
		this.game = game;
		this.port = port;
		this.gameName = gameName;
		this.expansionPack = expansionPack;
		this.numberOfBots = numberOfBots;
		this.maxPlayers = maxPlayers; 
		this.joinPassword = joinPassword;
		try {
			socket = new MulticastSocket(LanDiscoveryServer.MulticastGroupPort);
			socket.joinGroup(InetAddress.getByName(LanDiscoveryServer.MulticastGroupIP));
			packetHandler = new PacketCreator(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void start() {
		try {
			while (true) {
				byte[] buff = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buff,1024);
				socket.receive(packet);
				InetAddress senderIP = packet.getAddress();
				int senderPort = packet.getPort();
				
				packet = packetHandler.createResponse(packet);
				packet.setAddress(senderIP);
				packet.setPort(senderPort);
				socket.send(packet);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
class PacketCreator{
	boolean fromClient;
	boolean hasPassword;
	byte[] ipAddress;
	int port;
	int expansionPack;
	int numberOfBots;
	int maxPlayers;
	byte[] gameName;
	GameServer game;
	LanDiscoveryServer discoveryServer;
	public PacketCreator(LanDiscoveryServer discoveryServer) {
		this.discoveryServer = discoveryServer;
		try {
			this.ipAddress = InetAddress.getByName(discoveryServer.ipAddress).getAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.port = discoveryServer.port;
		this.expansionPack = discoveryServer.expansionPack;
		this.numberOfBots = discoveryServer.numberOfBots;
		this.maxPlayers = discoveryServer.maxPlayers;
		this.hasPassword = discoveryServer.joinPassword != "";
		fromClient = false;
		this.gameName = discoveryServer.gameName.getBytes();
		this.game = discoveryServer.game;
	}
	public PacketCreator() {};
	
	// Format for packets:
	// sending back to client
	/* {
	// 		byte 0: {bit 0: isFromClient, bit 1: hasJoinPassword}
	//      byte 1 - 5: IP address
	 * 		byte 6 - 7: port
	 * 		byte 8: expansion packs used
	 * 		byte 9: number of bots
	 * 		byte 10: maxPlayers
	 * 		byte 11: numPlayersJoined
	 * 		byte 12 - 1023: gameName
	 */
	// }
	/* Sent by client:
	 * { byte 0: determines if sent from a client
	 * 	 byte 1-1023: irrelevant 
	 * }
	 * 
	 * */
	public DatagramPacket createResponse(DatagramPacket packet) { // constructs a server response, if requested by a client
		byte[] data = packet.getData();
		
		if ((data[0] & 0x1) == 1) { // if is from client
			data = new byte[1024];
			
			data[0] = (byte) ((fromClient?1:0)+ ((hasPassword ? 1:0) <<1));
			for (int i = 0; i < 4; i++) {
				data[1+i] = ipAddress[i];
			}
			byte[] bytes = ByteBuffer.allocate(2).putInt(port).array();
			data[6] = bytes[0];
			data[7] = bytes[7];
			data[8] = (byte) expansionPack;
			data[9] = (byte)numberOfBots; 
			data[10] = (byte) maxPlayers;
			data[11] = (byte) game.players.size();
			for(int i = 12; i < Integer.min(1024,12 + gameName.length); i++) {
				data[12 + i] = gameName[i];
			}
			DatagramPacket ret = new DatagramPacket(data, 1024);
			return ret;
		}
		
		return null;
	}
	static PacketCreator fromPacket(DatagramPacket packet) { // allows a server response to be interpreted
		PacketCreator ret = new PacketCreator();
		byte[] data = packet.getData();
		
		if ((data[0] & 0x1) != 1) { // if is not from client
			data = new byte[1024];
			
		}
		
		return ret;
	}
}