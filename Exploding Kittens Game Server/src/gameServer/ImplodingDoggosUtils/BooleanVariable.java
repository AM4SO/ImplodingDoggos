package gameServer.ImplodingDoggosUtils;

import java.util.function.Consumer;

public class BooleanVariable{
	protected boolean value;
	public boolean defaultVal;
	protected Consumer<Object> onSet;
	public Object arg;
	BooleanVariable(boolean value, Consumer<Object> setFunc) {
		this.value = value;
		defaultVal = value;
		onSet = setFunc;
	}
	public BooleanVariable(boolean value) {
		this.value = value;
		defaultVal = value;
		onSet = this::get;
	}
	public boolean set() {
		value = !defaultVal;
		onSet.accept(this.arg);
		return value;
	}public boolean set(Object arg) {
		this.arg = arg;
		return set();
	}
	public boolean reset() {
		value = defaultVal;
		return value;
	}
	public boolean get() {
		return value;
	}public boolean get(Object thing) {
		return value;
	}
	
}