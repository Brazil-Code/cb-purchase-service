package br.com.brazilcode.cb.purchase.exception;

import br.com.brazilcode.cb.libs.model.PurchaseRequest;

/**
 * Classe responsável por configurar uma exceção personalizada para validação de {@link PurchaseRequest}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 8 de mar de 2020 16:33:03
 * @version 1.0
 */
public class PurchaseRequestValidationException extends PurchaseRequestServiceException {

	private static final long serialVersionUID = 2719472012150424145L;

	public PurchaseRequestValidationException() {
		super();
	}

	public PurchaseRequestValidationException(String message) {
		super(message);
	}

	public PurchaseRequestValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PurchaseRequestValidationException(Throwable cause) {
		super(cause);
	}

}
