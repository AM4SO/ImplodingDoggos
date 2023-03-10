package gameServer.clientSide;

import java.net.Inet4Address;

public class LanRemoteGameAdapter extends RemoteGameAdapter {
	public LanRemoteGameAdapter(Inet4Address ip, int port) {
		super(ip,port, RemoteGameAdapter.Random.nextInt(), ImplodingDoggosUser.RandomUser(LanRemoteGameAdapter.Random));
	}
}
