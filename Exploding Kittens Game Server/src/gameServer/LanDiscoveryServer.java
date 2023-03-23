package gameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class LanDiscoveryServer{/// packet size = 1024
	public static final String MulticastGroupIP = "234.163.014.111";
	public static final int MulticastGroupPort = 25365;
	
	SocketAddress multicastAddress;
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
	boolean discoveryServerClosed;
	public LanDiscoveryServer(int port, String gameName, int expansionPack, int numberOfBots, int maxPlayers, String joinPassword, GameServer game) {
		this.game = game;
		this.port = port;
		this.gameName = gameName.trim();
		this.expansionPack = expansionPack;
		this.numberOfBots = numberOfBots;
		this.maxPlayers = maxPlayers; 
		this.joinPassword = joinPassword;
		discoveryServerClosed = false;
		try {
			multicastAddress = new InetSocketAddress(InetAddress.getByName(LanDiscoveryServer.MulticastGroupIP),
					MulticastGroupPort);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			socket = new MulticastSocket(MulticastGroupPort);
			socket.joinGroup(InetAddress.getByName(LanDiscoveryServer.MulticastGroupIP));
			packetHandler = new PacketCreator(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void stop() {
		discoveryServerClosed = true;
		socket.close();
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
			if(!discoveryServerClosed)
				e.printStackTrace();
		}
	}
}