package gameServer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class EventSystem {
	public static Event cardNeutralised = new Event();
	public static Event cardDrawn = new Event();
	public static Event explodingKittenCardReplaced = new Event();
	public static Event tryPlayCard = new Event();
	public static Event tryDrawCard = new Event();
	public static Event playerConnected = new Event();
	public static Event playerRequestReceived = new Event();
	public static Event playerDied = new Event();
	public static void Initialise() {
		cardNeutralised.onInvoked.add(GameServer::onCardNeutralised);
		cardDrawn.onInvoked.add(GameServer::onCardDrawn);
		explodingKittenCardReplaced.onInvoked.add(GameServer::onExplodingKittenReplaced);
		tryPlayCard.onInvoked.add(GameServer::onTryPlayCard);
		tryDrawCard.onInvoked.add(GameServer::onTryDrawCard);
		playerConnected.onInvoked.add(GameServer::onPlayerConnected);
		//playerRequestReceived.onInvoked.add(GameServer::onPlayerRequestReceived);
		playerRequestReceived.onInvoked.add((obj) -> {GameServer.game.requestHandler.onPlayerRequestReceived(obj);});
		playerDied.onInvoked.add(GameServer::onPlayerDied);
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
			else r.accept(arg);;
		}
	}public void invoke(Object arg) {
		invoke(arg, true);
	}public void invokeSync(Object arg) {
		invoke(arg, false);
	}
}