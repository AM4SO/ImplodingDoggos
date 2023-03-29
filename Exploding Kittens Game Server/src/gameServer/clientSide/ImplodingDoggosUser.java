package gameServer.clientSide;

import java.util.Random;

public class ImplodingDoggosUser {
	public final long userId, sessionToken;
	public String username;
	public ImplodingDoggosUser(long userId, long sessionId) {
		this.userId = userId;
		this.sessionToken = sessionId;
	}
	
	public static ImplodingDoggosUser RandomUser(Random rand) {
		return new ImplodingDoggosUser(rand.nextLong(), rand.nextLong());
	}public static ImplodingDoggosUser RandomUser() {
		Random rand = new Random();
		return new ImplodingDoggosUser(rand.nextLong(), rand.nextLong());
	}
}
