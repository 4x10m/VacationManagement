package core.exceptions;

@SuppressWarnings("serial")
public class InvalidCredentialException extends Exception {
	public InvalidCredentialException() {
	}

	public InvalidCredentialException(String arg0) {
		super(arg0);
	}

	public InvalidCredentialException(Throwable arg0) {
		super(arg0);
	}

	public InvalidCredentialException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidCredentialException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
