package gameServer;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 25565;
		if (args.length > 0) {
			Integer.decode(args[0]);
		}
		gameServer.GameServer x = new gameServer.GameServer(port); // args[0] = port
		x.startServer();//start doing shit init.
	}

}
