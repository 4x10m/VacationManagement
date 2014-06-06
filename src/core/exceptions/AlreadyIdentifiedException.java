package core.exceptions;

@SuppressWarnings("serial")
public class AlreadyIdentifiedException extends Exception {
	public AlreadyIdentifiedException() {
		super("this entity already have an id");
	}
}
