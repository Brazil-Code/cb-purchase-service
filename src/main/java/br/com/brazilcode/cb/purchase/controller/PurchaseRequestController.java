package br.com.brazilcode.cb.purchase.controller;

import static br.com.brazilcode.cb.libs.constants.ApiResponseConstants.VALIDATION_ERROR_RESPONSE;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import br.com.brazilcode.cb.libs.enumerator.LogActivityTypeEnum;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.PurchaseRequest;
import br.com.brazilcode.cb.libs.model.api.response.BadRequestResponseObject;
import br.com.brazilcode.cb.libs.model.api.response.CreatedResponseObject;
import br.com.brazilcode.cb.libs.model.api.response.InternalServerErrorResponseObject;
import br.com.brazilcode.cb.libs.model.api.response.RestIntegrationErrorResponse;
import br.com.brazilcode.cb.purchase.dto.PurchaseRequestDTO;
import br.com.brazilcode.cb.purchase.exception.PurchaseRequestValidationException;
import br.com.brazilcode.cb.purchase.service.PurchaseRequestService;
import br.com.brazilcode.cb.purchase.service.integration.administration.LogIntegrationService;

/**
 * Classe responsável por expor as APIs para PurchaseRequest.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 6 de mar de 2020 12:32:49
 * @version 1.2
 */
@RestController
@RequestMapping("purchase-request")
public class PurchaseRequestController implements Serializable {

	private static final long serialVersionUID = 4295218504465659187L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseRequestController.class);

	@Autowired
	private PurchaseRequestService purchaseRequestService;

	@Autowired
	private LogIntegrationService logIntegrationService;

	/**
	 * Método responsável por buscar uma {@link PurchaseRequest}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 */
	@GetMapping(path = "{id}")
	public ResponseEntity<?> findById(@PathVariable("id") final Long id) {
		final String method = "[ PurchaseRequestController.findById ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Calling purchaseRequestService.verifyIfExists - ID: " + id);
			final PurchaseRequest purchaseRequest = purchaseRequestService.verifyIfExists(id);

			LOGGER.debug(method + "Registering activity log");
			final String description = LogActivityTypeEnum.SEARCH.getDescription() + " for the Purchase Request: " + id;
			this.logIntegrationService.createLog(description);
			return new ResponseEntity<PurchaseRequest>(purchaseRequest, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			return new ResponseEntity<>(new InternalServerErrorResponseObject(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	/**
	 * Método responsável por gravar uma {@link PurchaseRequest}. Rollback em caso de exceção
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PurchaseRequestDTO}
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> save(HttpServletRequest requestContext, @Valid @RequestBody final PurchaseRequestDTO purchaseRequestDTO) {
		final String method = "[ PurchaseRequestController.save ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Calling PurchaseRequestService.save... sending: " + purchaseRequestDTO.toString());
			PurchaseRequest purchaseRequest = this.purchaseRequestService.save(requestContext.getHeader("Authorization"), purchaseRequestDTO);

			LOGGER.debug(method + "Registering activity log");
			final String description = LogActivityTypeEnum.CREATE.getDescription() + " a new Purchase Request: " + purchaseRequest.getId();
			this.logIntegrationService.createLog(description);
			return new ResponseEntity<>(new CreatedResponseObject(purchaseRequest.getId()), HttpStatus.CREATED);
		} catch (PurchaseRequestValidationException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (ResourceNotFoundException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (HttpClientErrorException.NotFound e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage();
			LOGGER.error(method + errorMessage, e);
			return new ResponseEntity<>(new BadRequestResponseObject(errorMessage), HttpStatus.BAD_REQUEST);
		} catch (RestClientException e) {
			LOGGER.error(method + e.getMessage(), e);
			return new ResponseEntity<>(new RestIntegrationErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			return new ResponseEntity<>(new InternalServerErrorResponseObject(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

}
