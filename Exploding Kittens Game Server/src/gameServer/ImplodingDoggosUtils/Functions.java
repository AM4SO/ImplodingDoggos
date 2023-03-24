package gameServer.ImplodingDoggosUtils;

public abstract class Functions {
	public static boolean waitTimeOrTrue(long milliseconds, BooleanVariable boolVar,
			boolean setOnEnd) {
		long start = System.nanoTime();
		milliseconds *= 1000000;
		long now = System.nanoTime();
		while (now - start < milliseconds && 
				(boolVar.value==boolVar.defaultVal)) {
			now = System.nanoTime();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("Sleep ended early");
				//e.printStackTrace();
			}
		}
		boolean ret = boolVar.value != boolVar.defaultVal;
		if (setOnEnd) boolVar.set();
		return ret;
	}public static boolean waitTimeOrTrue(int milliseconds, BooleanVariable boolVar) {
		return waitTimeOrTrue(milliseconds, boolVar, false);
	}
	public static void await(int milliseconds) {
		Object x = new Object();
		try {
			x.wait(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static boolean waitForFalse(BooleanVariable playerJoined) {
		// TODO Auto-generated method stub
		while (playerJoined.value != playerJoined.defaultVal) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
}