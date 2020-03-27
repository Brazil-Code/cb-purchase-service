package br.com.brazilcode.cb.purchase.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.brazilcode.cb.libs.enumerator.PurchaseRequestStatusEnum;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.PriceQuotation;
import br.com.brazilcode.cb.libs.model.PurchaseRequest;
import br.com.brazilcode.cb.libs.repository.PurchaseRequestRepository;
import br.com.brazilcode.cb.purchase.dto.PriceQuotationDTO;
import br.com.brazilcode.cb.purchase.dto.PurchaseRequestDTO;
import br.com.brazilcode.cb.purchase.exception.PurchaseRequestValidationException;
import br.com.brazilcode.cb.purchase.service.integration.administration.UserIntegrationService;

/**
 * Classe responsável por aplicar as regras de negócio para {@link PurchaseRequest}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 6 de mar de 2020 15:59:04
 * @version 1.2
 */
@Service
public class PurchaseRequestService implements Serializable {

	private static final long serialVersionUID = -5896249497018734119L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseRequestService.class);

	/**
	 * Todas as PurchaseRequest precisam ter no mínumo 3 PriceQuotation
	 */
	public static final int MINIMUM_PRICE_QUOTATION = 3;

	/**
	 * Todas as PurchaseRequest podem ter no máximo 5 PriceQuotation
	 */
	public static final int MAXIMUM_PRICE_QUOTATION = 5;

	@Autowired
	private PurchaseRequestRepository purchaseRequestDAO;

	@Autowired
	private UserIntegrationService userIntegrationService;

	@Autowired
	private PriceQuotationService priceQuotationService;

	/**
	 * Método responsável por salvar um {@link PurchaseRequest} no banco de dados aplicando as regras de negócio.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PurchaseRequestDTO}
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PurchaseRequest save(String authorization, PurchaseRequestDTO purchaseRequestDTO) throws Exception {
		final String method = "[ PurchaseRequestService.save ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Validating mandatory fields");
			this.validateMandatoryFields(purchaseRequestDTO);

			LOGGER.debug(method + "Calling PriceQuotationService.save");
			List<PriceQuotation> priceQuotations = this.priceQuotationService.save(purchaseRequestDTO);

			LOGGER.debug(method + "Converting: " + purchaseRequestDTO.toString() + " to entity");
			PurchaseRequest purchaseRequest = this.convertDtoToEntity(authorization, purchaseRequestDTO);

			LOGGER.debug(method + "Adding all the price quotations in the PurchaseRequest's list");
			purchaseRequest.getPriceQuotations().addAll(priceQuotations);

			LOGGER.debug(method + "Saving: " + purchaseRequest.toString());
			return this.purchaseRequestDAO.save(purchaseRequest);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	/**
	 * Método responsável por validar os campos obrigatórios para {@link PurchaseRequest}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PurchaseRequestDTO}
	 * @throws {@link PurchaseRequestValidationException}
	 */
	public void validateMandatoryFields(PurchaseRequestDTO purchaseRequestDTO)
			throws PurchaseRequestValidationException, ResourceNotFoundException {
		final String method = "[ PurchaseRequestService.validateMandatoryFields ] - ";
		LOGGER.debug(method + "BEGIN");

		StringBuilder warnings = new StringBuilder();

		if (purchaseRequestDTO != null) {
			if (purchaseRequestDTO.getCreateUser() == null) {
				warnings.append(", Field \'createUser\' cannot be null");
			}

			if (StringUtils.isBlank(purchaseRequestDTO.getPurchaseItem())) {
				warnings.append(", Field \'purchaseItem\' cannot be null");
			}

			List<PriceQuotationDTO> priceQuotations = purchaseRequestDTO.getPriceQuotations();
			if (priceQuotations.size() >= MINIMUM_PRICE_QUOTATION) {
				priceQuotations.forEach((pq) -> {
					if (StringUtils.isBlank(pq.getLink())) {
						warnings.append(", Field \'link\' cannot be null");
					}

					if (pq.getUnitValue() <= 0) {
						warnings.append(", Field \'unitValue\' cannot be negative");
					}

					if (pq.getAmount() <= 0) {
						warnings.append(", Field \'amount\' cannot be negative");
					}

					if (pq.getTotalValue() <= 0) {
						warnings.append(", Field \'totalValue\' cannot be negative");
					}
				});
			} else {
				warnings.append(", Purchase Requests must have at least 3 price quotations");
			}

			if (priceQuotations.size() > MAXIMUM_PRICE_QUOTATION) {
				warnings.append(", Purchase Requests can have 5 price quotations maximum");
			}
		} else {
			warnings.append(", Object PurchaseRequest cannot be null");
		}

		if (warnings.length() > 1) {
			LOGGER.error(method + "Validation warnings" + warnings.toString());
			throw new PurchaseRequestValidationException(warnings.toString());
		}

		LOGGER.debug(method + "END");
	}

	/**
	 * Método responsável por converter um objeto {@link PurchaseRequestDTO} para entidade {@link PurchaseRequest}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PurchaseRequestDTO}
	 * @return {@link PurchaseRequest} com os atributos preenchidos com os dados do objeto DTO
	 * @throws Exception
	 */
	private PurchaseRequest convertDtoToEntity(String authorization, PurchaseRequestDTO purchaseRequestDTO) throws Exception {
		final String method = "[ PurchaseRequestService.convertDtoToEntity ] - ";
		LOGGER.debug(method + "BEGIN");

		PurchaseRequest purchaseRequest = new PurchaseRequest();

		try {
			LOGGER.debug(method + "Loading PurchaseRequest");
			purchaseRequest
					.setCreateUser(this.userIntegrationService.verifyIfExists(authorization, purchaseRequestDTO.getCreateUser()));
			purchaseRequest.setPurchaseItem(purchaseRequestDTO.getPurchaseItem());
			purchaseRequest.setStatus(PurchaseRequestStatusEnum.PENDING.getId());
			purchaseRequest.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		}

		LOGGER.debug(method + "END... Returning: " + purchaseRequest.toString());
		return purchaseRequest;
	}

	/**
	 * Método responsável por verificar se a {@link PurchaseRequest} existe pelo ID informado.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return {@link PurchaseRequest} caso o ID seja encontrado na base de dados
	 */
	public PurchaseRequest verifyIfExists(Long id) {
		return purchaseRequestDAO.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(", Purchase Request not found for the given ID: " + id));
	}

}
