package feng.shi.exception;

public class TodoNotFoundException extends Exception {

	private static final long serialVersionUID = 6737802258693255945L;

	public TodoNotFoundException() {
	}

	public TodoNotFoundException(String message) {
		super(message);
	}

	public TodoNotFoundException(Throwable t) {
		super(t);
	}

}
