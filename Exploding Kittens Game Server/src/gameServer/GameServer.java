package gameServer;

import java.io.IOException;
import java.net.*;
import java.net.Socket;
import java.util.concurrent.ThreadFactory;
import java.util.List;
import java.util.concurrent.*;

public class GameServer {
	public java.util.concurrent.Executor thread;
	public List<Player> players;
	public CardStack drawPile;
	public GameServer(int port) {
		players = List.of();
		players.add(new ComputerPlayer());
		players.add(new ComputerPlayer());
		
		drawPile = new CardStack();
		
		//ServerListener listener = new ServerListener(port);
		//listener.run();
		
	}
	public void startServer() {

	}
	public void awaitPlayers() {
		
	}
}
