package gameServer;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class HumanPlayer extends Player {
	static protected MultiSetterBooleanVariable PlrCreating = new MultiSetterBooleanVariable(false);
	public static HumanPlayer getPlayerByUserId(long userId) {
		for (int i = 0; i < Player.players.size(); i++){
			Player p = Player.players.get(i);
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
		System.out.println(" has joined the game");
	}
	public static Player create(long userId2, ObjectOutputStream objectOutputStream) {
		ExplodingKittensUtils.waitForFalse(PlrCreating);
		if (PlrCreating.value) {
			ExplodingKittensUtils.waitForFalse(PlrCreating);
		}
		PlrCreating.set();
		return null;
	}
	public static void newPlayer(long userId, ObjectOutputStream stream) {
		
	}

}
