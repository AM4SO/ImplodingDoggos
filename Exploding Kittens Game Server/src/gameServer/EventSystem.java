package gameServer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class EventSystem {
	public static Event cardNeutralised = new Event();
	public static Event cardDrawn = new Event();
	public static Event explodingKittenCardReplaced = new Event();
	public static Event cardPlayed = new Event();
	public static Event tryDrawCard = new Event();
	public static Event playerConnected = new Event();
	public static void Initialise() {
		cardNeutralised.onInvoked.add(GameServer::onCardNeutralised);
		cardDrawn.onInvoked.add(GameServer::onCardDrawn);
		explodingKittenCardReplaced.onInvoked.add(GameServer::onExplodingKittenReplaced);
		cardPlayed.onInvoked.add(GameServer::onCardPlayed);
		tryDrawCard.onInvoked.add(GameServer::onTryDrawCard);
		playerConnected.onInvoked.add(GameServer::onPlayerConnected);
	}
	public EventSystem() {
		
	}
	
}
class Event{
	ArrayList<Consumer<Object>> onInvoked;
	public Event() {
		onInvoked = new ArrayList<Consumer<Object>>();
	}
	public void invoke(Object arg, boolean newThread) {
		for (Consumer<Object> r: onInvoked) {
			if (newThread) new Thread( () -> {r.accept(arg);} ).start();
			else new Thread( () -> {r.accept(arg);} ).run();
		}
	}public void invoke(Object arg) {
		invoke(arg, true);
	}public void invokeSync(Object arg) {
		invoke(arg, false);
	}
}