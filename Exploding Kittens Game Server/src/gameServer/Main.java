package gameServer;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		gameServer.GameServer x = new gameServer.GameServer(Integer.decode(args[0])); // args[0] = port
		x.startServer();//start doing shit init.
	}

}
