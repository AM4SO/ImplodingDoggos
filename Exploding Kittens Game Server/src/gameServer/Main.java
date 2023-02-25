package gameServer;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 25565;
		boolean DEBUGGING = true;
		if (args != null && args.length > 0) {
			port = Integer.decode(args[0]);
			if (args.length >= 2) {
				DEBUGGING = Boolean.getBoolean(args[1]);
			}
		}
		gameServer.GameServer x = new gameServer.GameServer(port); // args[0] = port
		TestClient c = null;
		TestClient b = null;
		if (DEBUGGING) {
			c = new TestClient(4534897343l, port);
			b = new TestClient(443l, port);
		}
		new Thread (() -> {x.init(); x.startServer();}).start();;
		if(DEBUGGING) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c.start();
			b.start();
		}
		//x.init();
		//x.startServer();//start doing shit init.
	}

}
