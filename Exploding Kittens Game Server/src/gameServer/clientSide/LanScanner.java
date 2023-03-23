package gameServer.clientSide;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import gameServer.LanDiscoveryServer;
import gameServer.PacketCreator;

public class LanScanner{
	private final static String MulticastGroupIp = LanDiscoveryServer.MulticastGroupIP;
	private static final int MulticastGroupPort = LanDiscoveryServer.MulticastGroupPort;

	DatagramSocket discovererSocket;
	public ArrayList<PacketCreator> responses;
	
	public LanScanner() {
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
		Thread t = new Thread(() -> {receiveResponses();});
		t.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		t.interrupt();
	}
	public void receiveResponses() {
		this.responses = new ArrayList<PacketCreator>();
		byte[] buff = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buff, 1024);
		try {
			discovererSocket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PacketCreator response = PacketCreator.fromPacket(packet);
		responses.add(response);
	}
}