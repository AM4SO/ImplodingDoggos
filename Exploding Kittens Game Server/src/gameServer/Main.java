package gameServer;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 25566;
		if (args.length > 0) {
			Integer.decode(args[0]);
		}
		gameServer.GameServer x = new gameServer.GameServer(port); // args[0] = port
		TestClient c = new TestClient(4534897343l, port);
		TestClient b = new TestClient(443l, port);
		new Thread (() -> {x.init(); x.startServer();}).start();;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.start();
		b.start();
		//x.init();
		//x.startServer();//start doing shit init.
	}

}
