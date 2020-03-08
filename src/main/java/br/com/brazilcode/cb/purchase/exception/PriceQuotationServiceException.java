package br.com.brazilcode.cb.purchase.exception;

import br.com.brazilcode.cb.purchase.service.PriceQuotationService;

/**
 * Classe responsável por configurar uma exceção personalizada para a classe {@link PriceQuotationService}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 8 de mar de 2020 17:48:52
 * @version 1.0
 */
public class PriceQuotationServiceException extends Exception {

	private static final long serialVersionUID = -1535937550574563389L;

	public PriceQuotationServiceException() {
		super();
	}

	public PriceQuotationServiceException(String message) {
		super(message);
	}

	public PriceQuotationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PriceQuotationServiceException(Throwable cause) {
		super(cause);
	}

}
