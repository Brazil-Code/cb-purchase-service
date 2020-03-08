package br.com.brazilcode.cb.purchase.controller;

import java.io.Serializable;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brazilcode.cb.purchase.dto.PurchaseRequestDTO;
import br.com.brazilcode.cb.purchase.exception.PurchaseRequestValidationException;
import br.com.brazilcode.cb.purchase.service.PurchaseRequestService;

import static br.com.brazilcode.cb.purchase.constants.ApiResponseConstants.*;

/**
 * Classe responsável por expor as APIs para PurchaseRequest.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 6 de mar de 2020 12:32:49
 * @version 1.0
 */
@RestController
@RequestMapping("purchase-request")
public class PurchaseRequestController implements Serializable {

	private static final long serialVersionUID = 4295218504465659187L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseRequestController.class);

	@Autowired
	private PurchaseRequestService purchaseRequestService;

	/**
	 * Método responsável por gravar uma {@link PurchaseRequest}. Rollback em caso de exceção
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PurchaseRequestDTO}
	 * @return
	 */
	@PostMapping
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> save(@Valid @RequestBody final PurchaseRequestDTO purchaseRequestDTO) {
		String method = "[ PurchaseRequestController.save ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Calling PurchaseRequestService.save... sending: " + purchaseRequestDTO.toString());
			this.purchaseRequestService.save(purchaseRequestDTO);
		} catch (PurchaseRequestValidationException e) {
			LOGGER.error(method + e, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error(method + e, e.getMessage());
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		LOGGER.debug(method + "END");
		return new ResponseEntity<>(CREATED_RESPONSE, HttpStatus.CREATED);
	}

}
