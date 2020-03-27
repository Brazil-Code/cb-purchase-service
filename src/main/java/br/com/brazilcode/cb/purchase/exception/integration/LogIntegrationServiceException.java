package br.com.brazilcode.cb.purchase.exception.integration;

/**
 * Classe responsável por configurar uma exceção personalizada para o módulo de administração.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 26 de mar de 2020 23:19:35
 * @version 1.0
 */
public class LogIntegrationServiceException extends Exception {

	private static final long serialVersionUID = 596130936590524127L;

	public LogIntegrationServiceException() {
		super();
	}

	public LogIntegrationServiceException(String message) {
		super(message);
	}

	public LogIntegrationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogIntegrationServiceException(Throwable cause) {
		super(cause);
	}

}
