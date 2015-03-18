package fr.gro.exception;

public class JsonParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Throwable exception;
	private String message;

	public JsonParserException(Throwable exception, String message) {
		super();
		this.exception = exception;
		this.message = message;
	}

	public JsonParserException(Throwable exception) {
		super();
		this.exception = exception;
		this.message = exception.getMessage();
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
