package gameServer;

import java.util.function.Consumer;

public class ExplodingKittensUtils {
	public static boolean waitTimeOrTrue(long milliseconds, BooleanVariable boolVar,
			boolean setOnEnd) {
		long start = System.nanoTime();
		milliseconds *= 1000000;
		while (System.nanoTime() - start < milliseconds && 
				(boolVar.value==boolVar.defaultVal)) {
			continue;
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
}

class BooleanVariable{
	boolean value;
	public boolean defaultVal;
	private Consumer<Void> onSet;
	public Object arg;
	BooleanVariable(boolean value, Consumer<Void> setFunc) {
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
		onSet.accept(null);
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
	}public boolean get(Void thing) {
		return value;
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