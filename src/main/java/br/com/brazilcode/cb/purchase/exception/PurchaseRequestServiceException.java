package br.com.brazilcode.cb.purchase.exception;

import br.com.brazilcode.cb.purchase.service.PurchaseRequestService;

/**
 * Classe responsável por configurar uma exceção personalizada para a classe {@link PurchaseRequestService}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 26 de mar de 2020 21:40:46
 * @version 1.0
 */
public class PurchaseRequestServiceException extends Exception {

	private static final long serialVersionUID = -332466114662268285L;

	public PurchaseRequestServiceException() {
		super();
	}

	public PurchaseRequestServiceException(String message) {
		super(message);
	}

	public PurchaseRequestServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PurchaseRequestServiceException(Throwable cause) {
		super(cause);
	}

}
