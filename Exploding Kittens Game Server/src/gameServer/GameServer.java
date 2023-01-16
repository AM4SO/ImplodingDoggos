package gameServer;

import java.io.IOException;
import java.net.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class GameServer {
	public java.util.concurrent.Executor thread;
	public ArrayList<Player> players;
	public CardStack drawPile;
	public int playerGo = 0;
	public GameServer(int port) {
		players = new ArrayList<Player>();
		players.add(new Player(this));
		players.add(new Player(this));
		
		drawPile = new CardStack();
		
		while (Player.totalPlayers - Player.playersDead > 1) {
			Player plr = players.get(playerGo);
			plr.startTurn();
			waitTimeOrTrue(10000, plr.hasTurn, false);
			plr.drawCard();
		}
		
	}
	public void startServer() {

	}
	public void awaitPlayers() {
		
	}
	public void waitTimeOrTrue(int milliseconds, BooleanVariable boolVar,
			boolean valueToSetOnComplete) {
		long start = System.nanoTime();
		while (System.nanoTime() - start < milliseconds && 
				!(boolVar.value==valueToSetOnComplete)) {
			continue;
		}
		boolVar.set();
	}
}

class BooleanVariable{
	boolean value;
	private boolean defaultVal;
	private Consumer<Void> onSet;
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
	}public boolean reset() {
		value = defaultVal;
		return value;
	}
	public boolean get() {
		return value;
	}public boolean get(Void thing) {
		return value;
	}
	
}
