package gameServer;

import java.io.ObjectOutputStream;
import java.net.Socket;

import gameServer.ImplodingDoggosUtils.Functions;
import gameServer.ImplodingDoggosUtils.MultiSetterBooleanVariable;

public class HumanPlayer extends Player {
	static protected MultiSetterBooleanVariable PlrCreating = new MultiSetterBooleanVariable(false);
	static protected boolean lock = false;
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
		userCommunicator = new HumanPlayerCommunicator(this,stream);
		System.out.print("Player with userId: ");
		System.out.print(userId);
		System.out.println(" has joined the game");
	}
	public static void PlayerCreationHandler() { ///  Should run on own thread after
		while (GameServer.game.waitingForPlayers) {// setting waitingForPlayers to true
			if (Functions.waitTimeOrTrue(3_000, PlrCreating)) {
				UIDOutputStreamPair args = (UIDOutputStreamPair) PlrCreating.arg;
				assert args!=null && PlrCreating != null;
				new HumanPlayer(args.UID, args.stream);
				PlrCreating.reset();
			}
		}
	}
	
	public static HumanPlayer create(long userId2, ObjectOutputStream objectOutputStream) {
		while (lock) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lock = true;
		HumanPlayer ret = new HumanPlayer(userId2, objectOutputStream);
		lock = false;
		return ret;
		/*PlrCreating.set(new UIDOutputStreamPair(userId2, objectOutputStream));
		HumanPlayer ret = null;
		while (ret == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ret = HumanPlayer.getPlayerByUserId(userId2);
			System.out.println("thing: ".concat(String.valueOf(userId2)));
		}
		return ret;*/
	}
}
class UIDOutputStreamPair{
	public long UID;
	public ObjectOutputStream stream;
	public UIDOutputStreamPair(long id, ObjectOutputStream s) {
		UID = id;
		stream = s;
	}
}
