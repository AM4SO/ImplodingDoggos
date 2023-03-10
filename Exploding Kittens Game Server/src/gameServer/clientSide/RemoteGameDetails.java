package gameServer.clientSide;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class RemoteGameDetails{
	Inet4Address ip;
	int port, gameId;
	public RemoteGameDetails(Inet4Address ip, int port, int gameId) {
		this.ip = ip;
		this.port = port;
		this.gameId = gameId;
	}
	public SocketAddress getSocketAddress() {
		return new InetSocketAddress(ip, port);
	}
}