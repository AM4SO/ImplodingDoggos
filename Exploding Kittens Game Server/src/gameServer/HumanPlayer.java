package gameServer;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class HumanPlayer extends Player {
	public static HumanPlayer getPlayerByUserId(long userId) {
		for (Player p : Player.players) {
			if (HumanPlayer.class.isInstance(p)) {
				HumanPlayer plr = (HumanPlayer) p;
				if(plr.userId == userId) return plr;
			}
		}
		
		return null;
	}
	
	public long userId;
	//private Socket socket;
	public HumanPlayer(long userId, ObjectOutputStream stream) {
		super();
		this.userId = userId;
		userCommunicator = new HumanPlayerCommunicator(stream);
		System.out.print("Player with userId: ");
		System.out.print(userId);
		System.out.print(" has joined the game");
	}

}
