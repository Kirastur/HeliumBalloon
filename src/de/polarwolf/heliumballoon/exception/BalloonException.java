package de.polarwolf.heliumballoon.exception;

public class BalloonException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final String JAVA_EXCEPTION = "Java Exception";
	
	protected final String contextName;
	protected final String errorName;
	protected final String errorDetailText;

	
	public BalloonException(String contextName, String errorName, String errorDetailText) {
		super(buildMessage(contextName, errorName, errorDetailText), null, false, false);
		this.contextName = contextName;
		this.errorName = errorName;
		this.errorDetailText = errorDetailText;
	}
	
	
	public BalloonException(String contextName, String errorName, String errorDetailText, Throwable cause) {
		super(buildMessage(contextName, errorName, errorDetailText), cause, false, false);
		this.contextName = contextName;
		this.errorName = errorName;
		this.errorDetailText = errorDetailText;
	}

	
	public String getContextName() {
		return contextName;
	}


	public String getErrorName() {
		return errorName;
	}


	public String getErrorDetailText() {
		return errorDetailText;
	}


	protected static String buildMessage(String contextName, String errorName, String errorDetailText) {
		String message = errorName;

		if ((contextName != null) && (!contextName.isEmpty())) {
			message = contextName + ": " + message; 
		}

		if ((errorDetailText != null) && (!errorDetailText.isEmpty())) {
			message = message + ": " +  errorDetailText; 
		}

		return message;
	}

}
