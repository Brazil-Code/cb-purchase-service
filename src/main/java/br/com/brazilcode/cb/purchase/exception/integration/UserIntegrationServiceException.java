package br.com.brazilcode.cb.purchase.exception.integration;

public class UserIntegrationServiceException extends Exception {

	private static final long serialVersionUID = -2440824036920686921L;

	public UserIntegrationServiceException() {
		super();
	}

	public UserIntegrationServiceException(String message) {
		super(message);
	}

	public UserIntegrationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserIntegrationServiceException(Throwable cause) {
		super(cause);
	}

}
