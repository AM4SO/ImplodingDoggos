package gameServer.clientSide;

import java.io.IOException;

public class IOResult{
	static final IOResult Success = new IOResult(true);
	
	public final boolean success;
	public final IOException exception;
	public IOResult(boolean isSuccess) {
		success = isSuccess;
		exception = null;
	}
	public IOResult(IOException e) {
		success = false;
		exception = e;
	}
}