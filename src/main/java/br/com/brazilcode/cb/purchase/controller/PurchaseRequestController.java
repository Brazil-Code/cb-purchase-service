package br.com.brazilcode.cb.purchase.controller;

import static br.com.brazilcode.cb.libs.constants.ApiResponseConstants.VALIDATION_ERROR_RESPONSE;

import java.io.Serializable;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import br.com.brazilcode.cb.purchase.exception.integration.UserIntegrationServiceException;
import br.com.brazilcode.cb.purchase.service.PurchaseRequestService;
import br.com.brazilcode.cb.purchase.service.integration.administration.LogIntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Classe responsável por expor as APIs para PurchaseRequest.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 6 de mar de 2020 12:32:49
 * @version 1.3
 */
@Api(value = "REST API for Purchase Requests")
@CrossOrigin(origins = "*")
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
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return a Purchase Request"),
			@ApiResponse(code = 400, message = "Purchase Request not found for the given ID"),
			@ApiResponse(code = 500, message = "Unexpected internal error") 
		})
	@ApiOperation(value = "Search for a Purchase Request in database with the given ID")
	public ResponseEntity<?> getById(@PathVariable("id") final Long id) {
		final String method = "[ PurchaseRequestController.findById ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Calling purchaseRequestService.verifyIfExists - ID: " + id);
			final PurchaseRequest purchaseRequest = this.purchaseRequestService.verifyIfExists(id);

			LOGGER.debug(method + "Registering activity log");
			final String description = LogActivityTypeEnum.SEARCH.getDescription() + " pelo Pedido de Compra: " + id;
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
	 * Método responsável por retornar uma Lista Paginada com todos os {@link PurchaseRequest} do usuário informado.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @return
	 */
	@GetMapping
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return a paged list with all the Purchase Requests"),
			@ApiResponse(code = 400, message = "User not found for the given ID"),
			@ApiResponse(code = 500, message = "Unexpected internal error") 
		})
	@ApiOperation(value = "Return a paged list all of the Purchase Requests created by the given user ID")
	public ResponseEntity<?> getPaginated(@RequestParam(value = "user", required = true) final int userId, Pageable pageable) {
		final String method = "[ PurchaseRequestController.getPaginated ] - ";
		LOGGER.debug(method + "BEGIN - UserID received: " + userId);

		try {
			LOGGER.debug(method + "Calling purchaseRequestService.findPaginated");
			Page<PurchaseRequest> page = this.purchaseRequestService.findPaginatedByUserId(userId, pageable);

			LOGGER.debug(method + "Registering activity log");
			String description = LogActivityTypeEnum.SEARCH.getDescription() + " uma lista de Pedidos de Compra";
			this.logIntegrationService.createLog(description);

			return new ResponseEntity<>(page, HttpStatus.OK);
		} catch (UserIntegrationServiceException e) {
			final String errorMessage = VALIDATION_ERROR_RESPONSE + e.getMessage().replace("404 : [", "");
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
	 * Método responsável por gravar uma {@link PurchaseRequest} no banco de dados. Rollback em caso de exceção
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PurchaseRequestDTO}
	 * @return
	 */
	@PostMapping
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Return the ID of the created Purchase Request"),
			@ApiResponse(code = 400, message = "Validation Error"),
			@ApiResponse(code = 500, message = "Unexpected internal error") 
		})
	@ApiOperation(value = "Register a new Purchase Request")
	public ResponseEntity<?> save(@Valid @RequestBody final PurchaseRequestDTO purchaseRequestDTO) {
		final String method = "[ PurchaseRequestController.save ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Calling PurchaseRequestService.save... sending: " + purchaseRequestDTO.toString());
			PurchaseRequest purchaseRequest = this.purchaseRequestService.save(purchaseRequestDTO);
			Long purchaseRequestId = purchaseRequest.getId();

			LOGGER.debug(method + "Registering activity log");
			String description = LogActivityTypeEnum.CREATE.getDescription() + " um novo Pedido de Compra: " + purchaseRequestId;
			this.logIntegrationService.createLog(description);

			return new ResponseEntity<>(new CreatedResponseObject(purchaseRequestId), HttpStatus.CREATED);
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
