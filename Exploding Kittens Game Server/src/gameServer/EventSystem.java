package gameServer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class EventSystem {
	public static Event cardNeutralised = new Event();
	public static Event cardDrawn = new Event();
	public static Event explodingKittenCardReplaced = new Event();
	public static Event cardPlayed = new Event();
	public static Event tryDrawCard = new Event();
	public static Event playerAdded = new Event();
	public static void Initialise() {
		cardNeutralised.onInvoked.add(GameServer::onCardNeutralised);
		cardDrawn.onInvoked.add(GameServer::onCardDrawn);
		explodingKittenCardReplaced.onInvoked.add(GameServer::onExplodingKittenReplaced);
		cardPlayed.onInvoked.add(GameServer::onCardPlayed);
		tryDrawCard.onInvoked.add(GameServer::onTryDrawCard);
		playerAdded.onInvoked.add(GameServer::onPlayerAdded);
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
			new Thread( () -> {r.accept(arg);} ).start();
		}
	}
}