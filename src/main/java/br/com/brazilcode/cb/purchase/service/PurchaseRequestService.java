package br.com.brazilcode.cb.purchase.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.brazilcode.cb.libs.enumerator.PurchaseRequestStatusEnum;
import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.PriceQuotation;
import br.com.brazilcode.cb.libs.model.PurchaseRequest;
import br.com.brazilcode.cb.libs.repository.PurchaseRequestRepository;
import br.com.brazilcode.cb.purchase.dto.PriceQuotationDTO;
import br.com.brazilcode.cb.purchase.dto.PurchaseRequestDTO;
import br.com.brazilcode.cb.purchase.exception.PurchaseRequestValidationException;

/**
 * Classe responsável por aplicar as regras de negócio para {@link PurchaseRequest}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 6 de mar de 2020 15:59:04
 * @version 1.0
 */
@Service
public class PurchaseRequestService implements Serializable {

	private static final long serialVersionUID = -5896249497018734119L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseRequestService.class);

	@Autowired
	private PurchaseRequestRepository purchaseRequestDAO;

	@Autowired
	private UserService userService;

	@Autowired
	private PriceQuotationService priceQuotationService;

	/**
	 * Método responsável por salvar um {@link PurchaseRequest} no banco de dados aplicando as regras de negócio.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PurchaseRequestDTO}
	 * @throws Exception
	 */
	public void save(PurchaseRequestDTO purchaseRequestDTO) throws Exception {
		String method = "[ PurchaseRequestService.save ] - ";
		LOGGER.debug(method + "BEGIN");

		try {
			LOGGER.debug(method + "Validating mandatory fields");
			this.validateMandatoryFields(purchaseRequestDTO);

			LOGGER.debug(method + "Calling PriceQuotationService.save");
			List<PriceQuotation> priceQuotations = this.priceQuotationService.save(purchaseRequestDTO);

			LOGGER.debug(method + "Converting: " + purchaseRequestDTO.toString() + " to entity");
			PurchaseRequest purchaseRequest = this.convertDtoToEntity(purchaseRequestDTO);

			LOGGER.debug(method + "Adding all the price quotations in the PurchaseRequest's list");
			purchaseRequest.getPriceQuotations().addAll(priceQuotations);

			LOGGER.debug(method + "Saving: " + purchaseRequest.toString());
			this.purchaseRequestDAO.save(purchaseRequest);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		}

		LOGGER.debug(method + "END");
	}

	/**
	 * Método responsável por validar os campos obrigatórios para {@link PurchaseRequest}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param purchaseRequestDTO
	 * @throws PurchaseRequestValidationException
	 */
	public void validateMandatoryFields(PurchaseRequestDTO purchaseRequestDTO) throws PurchaseRequestValidationException {
		String method = "[ PurchaseRequestService.validateMandatoryFields ] - ";
		LOGGER.debug(method + "BEGIN");

		StringBuilder warnings = new StringBuilder();

		if (purchaseRequestDTO != null) {
			if (purchaseRequestDTO.getCreateUser() == null) {
				warnings.append("\nField \'createUser\' cannot be null.");
			}

			if (StringUtils.isBlank(purchaseRequestDTO.getObservation())) {
				warnings.append("\nField \'observation\' cannot be null.");
			}

			List<PriceQuotationDTO> priceQuotations = purchaseRequestDTO.getPriceQuotations();
			if (priceQuotations.size() >= 3) {
				priceQuotations.forEach((pq) -> {
					if (StringUtils.isBlank(pq.getLink())) {
						warnings.append("\nField \'link\' cannot be null.");
					}

					if (pq.getUnitValue() <= 0) {
						warnings.append("\nField \'unitValue\' cannot be negative.");
					}

					if (StringUtils.isBlank(pq.getPurchaseItem())) {
						warnings.append("\nField \'purchaseItem\' cannot be null.");
					}

					if (pq.getAmount() <= 0) {
						warnings.append("\nField \'amount\' cannot be negative.");
					}

					if (pq.getTotalValue() <= 0) {
						warnings.append("\nField \'totalValue\' cannot be negative.");
					}
				});
			} else {
				warnings.append("\nPurchase Requests must have at least 3 price quotations");
			}
		} else {
			warnings.append("\nObject PurchaseRequest cannot be null");
		}

		if (warnings.length() > 1) {
			LOGGER.error(method + "Validation warnings: " + warnings.toString());
			throw new PurchaseRequestValidationException(warnings.toString());
		}

		LOGGER.debug(method + "END");
	}

	/**
	 * Método responsável por converter um objeto {@link PurchaseRequestDTO} para entidade {@link PurchaseRequest}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PurchaseRequestDTO}
	 * @return {@link PurchaseRequestDTO} com os atributos preenchidos com os dados do objeto DTO
	 */
	public PurchaseRequest convertDtoToEntity(PurchaseRequestDTO purchaseRequestDTO) {
		String method = "[ PurchaseRequestService.convertDtoToEntity ] - ";
		LOGGER.debug(method + "BEGIN");

		PurchaseRequest purchaseRequest = new PurchaseRequest();
		try {
			LOGGER.debug(method + "Loading PurchaseRequest");
			purchaseRequest.setCreateUser(this.userService.verifyIfExists(purchaseRequestDTO.getCreateUser()));
			purchaseRequest.setObservation(purchaseRequestDTO.getObservation());
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
		final Optional<PurchaseRequest> purchaseRequest = purchaseRequestDAO.findById(id);
		if (!purchaseRequest.isPresent())
			throw new ResourceNotFoundException("Purchase Request not found for the given ID: " + id);

		return purchaseRequest.get();
	}

}
