package br.com.brazilcode.cb.purchase.controller;

import java.io.Serializable;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brazilcode.cb.libs.model.PurchaseRequest;

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

	private String className = "PurchaseRequestController";

	@PostMapping
	public ResponseEntity<?> save() {
		String method = String.format("[ {}.save - ", className);
		LOGGER.debug(method + "BEGIN");
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

}
