package gameServer;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class ExplodingKittensUtils {
	public static boolean waitTimeOrTrue(long milliseconds, BooleanVariable boolVar,
			boolean setOnEnd) {
		long start = System.nanoTime();
		milliseconds *= 1000000;
		long now = System.nanoTime();
		while (now - start < milliseconds && 
				(boolVar.value==boolVar.defaultVal)) {
			now = System.nanoTime();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		boolean ret = boolVar.value != boolVar.defaultVal;
		if (setOnEnd) boolVar.set();
		return ret;
	}public static boolean waitTimeOrTrue(int milliseconds, BooleanVariable boolVar) {
		return waitTimeOrTrue(milliseconds, boolVar, false);
	}
	public static void await(int milliseconds) {
		Object x = new Object();
		try {
			x.wait(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static boolean waitForFalse(BooleanVariable playerJoined) {
		// TODO Auto-generated method stub
		while (playerJoined.value != playerJoined.defaultVal) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
}

class BooleanVariable{
	protected boolean value;
	public boolean defaultVal;
	protected Consumer<Object> onSet;
	protected Object arg;
	BooleanVariable(boolean value, Consumer<Object> setFunc) {
		this.value = value;
		defaultVal = value;
		onSet = setFunc;
	}
	BooleanVariable(boolean value) {
		this.value = value;
		defaultVal = value;
		onSet = this::get;
	}
	public boolean set() {
		value = !defaultVal;
		onSet.accept(this.arg);
		return value;
	}public boolean set(Object arg) {
		this.arg = arg;
		return set();
	}
	public boolean reset() {
		value = defaultVal;
		return value;
	}
	public boolean get() {
		return value;
	}public boolean get(Object thing) {
		return value;
	}
	
}
class MultiSetterBooleanVariable extends BooleanVariable{ // In some cases, a boolean variable can be set by many 
	private ConcurrentLinkedQueue<Object> setterQueue;///            different places in the program by different threads. 
	///////                  								 It is possible that it can be set twice at the same time
	//              										 causing one of the sets to be lost.
	/// In some cases, this won't matter, but sometimes, each 'set' must be recognised.
	/// This class utilises a thread-safe queue to store each additional set that occurs while the variable is already set.
	public MultiSetterBooleanVariable(boolean init, Consumer<Object> setFunc) {
		super(init, setFunc);
		setterQueue = new ConcurrentLinkedQueue<Object>();
	}public MultiSetterBooleanVariable(boolean init) {
		super(init);
		setterQueue = new ConcurrentLinkedQueue<Object>();
	}
	@Override
	public boolean set(Object arg) {
		if (value == defaultVal) {
			this.arg = arg;
			return super.set();
		}// if already set
		setterQueue.add(arg);
		return false;
	}
	@Override
	public boolean set() {
		if (value == defaultVal) {
			return super.set();
		}// if already set, add it to queue
		return set(null);
	}
	@Override
	public boolean reset() {
		if (setterQueue.peek() != null) { // if queue not empty, use next arg, don't reset this.value;
			this.arg = setterQueue.poll();
			this.value = !defaultVal;
			return value;
		}
		return super.reset();
	}
}
class PlrCardPair {
	Player player;
	Card card;
	public PlrCardPair(Player plr, Card c) {
		this.player = plr;
		this.card = c;
	}
}
class PlayerRequestPair{
	Player player;
	RequestContent request;
	public PlayerRequestPair(Player plr, RequestContent req) {
		player = plr;
		request = req;
	}
}
class PlayCardArgs{
	Player player;
	Card card;
	Object[] args;
	public PlayCardArgs(Player plr, Card c, Object[] arg) {
		player = plr;
		card = c;
		args = arg;
	}
}
class ClientMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	int playerId;
	ClientMessageContent cont;
}
class ClientMessageContent implements Serializable{
	private static final long serialVersionUID = 1L;
	ClientMessageType messageType;
}
enum ClientMessageType{
	TurnStarted
}