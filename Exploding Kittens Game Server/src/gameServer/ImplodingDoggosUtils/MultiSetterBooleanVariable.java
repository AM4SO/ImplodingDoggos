package gameServer.ImplodingDoggosUtils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class MultiSetterBooleanVariable extends BooleanVariable{ // In some cases, a boolean variable can be set by many 
	private ConcurrentLinkedQueue<Object> setterQueue;///            different places in the program by different threads. 
	///////                  								 It is possible that it can be set twice at the same time
	//              										 causing one of the sets to be lost.
	/// In some cases, this won't matter, but sometimes, each 'set' must be recognised.
	/// This class utilises a thread-safe queue to store each additional set that occurs while the variable is already set.
	public MultiSetterBooleanVariable(boolean init, Consumer<Object> setFunc) {
		super(init, setFunc);
		setterQueue = new ConcurrentLinkedQueue<Object>();
	}public MultiSetterBooleanVariable(boolean init) {
		super(init);
		setterQueue = new ConcurrentLinkedQueue<Object>();
	}
	@Override
	public boolean set(Object arg) {
		if (value == defaultVal) {
			this.arg = arg;
			return super.set();
		}// if already set
		setterQueue.add(arg);
		return false;
	}
	@Override
	public boolean set() {
		if (value == defaultVal) {
			return super.set();
		}// if already set, add it to queue
		return set(null);
	}
	@Override
	public boolean reset() {
		if (setterQueue.peek() != null) { // if queue not empty, use next arg, don't reset this.value;
			this.arg = setterQueue.poll();
			this.value = !defaultVal;
			return value;
		}
		return super.reset();
	}
}