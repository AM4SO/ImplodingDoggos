package gameServer;

import java.io.Console;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class EventSystem {
	public static Event cardNeutralised = new Event();
	public static Event cardDrawn = new Event();
	public static Event explodingKittenCardReplaced = new Event();
	public static void Initialise() {
		cardNeutralised.onInvoked.add(GameServer::onCardNeutralised);
		cardDrawn.onInvoked.add(GameServer::onCardDrawn);
		explodingKittenCardReplaced.onInvoked.add(GameServer::onExplodingKittenReplaced);
	}
	public EventSystem() {
		
	}
	
}
class Event{
	ArrayList<Consumer<Object>> onInvoked;
	public Event() {
		onInvoked = new ArrayList<Consumer<Object>>();
	}
	public void invoke(Object arg) {
		for (Consumer<Object> r: onInvoked) {
			new Thread( () -> {r.accept(arg);} ).run();
		}
	}
}