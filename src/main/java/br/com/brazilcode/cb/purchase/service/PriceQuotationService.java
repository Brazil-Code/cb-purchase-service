package br.com.brazilcode.cb.purchase.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.brazilcode.cb.libs.model.PriceQuotation;
import br.com.brazilcode.cb.libs.repository.PriceQuotationRepository;
import br.com.brazilcode.cb.purchase.dto.PriceQuotationDTO;
import br.com.brazilcode.cb.purchase.dto.PurchaseRequestDTO;

/**
 * Classe responsável por aplicar as regras de negócio para {@link PriceQuotation}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 8 de mar de 2020 17:06:23
 * @version 1.0
 */
@Service
public class PriceQuotationService implements Serializable {

	private static final long serialVersionUID = 6177896051698421977L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PriceQuotationService.class);

	@Autowired
	private PriceQuotationRepository priceQuotationDAO;

	/**
	 * Método responsável por salvar um {@link PriceQuotation} no banco de dados e retornar a lista de todas as
	 * {@link PriceQuotation}.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PriceQuotation}
	 * @return lista com todas as {@link PriceQuotation}
	 * @throws Exception
	 */
	public List<PriceQuotation> save(PurchaseRequestDTO purchaseRequestDTO) throws Exception {
		String method = "[ PriceQuotationService.save ] - ";
		LOGGER.debug(method + "BEGIN");

		List<PriceQuotationDTO> priceQuotationsDTO = purchaseRequestDTO.getPriceQuotations();
		List<PriceQuotation> priceQuotations = new ArrayList<PriceQuotation>();
		try {
			priceQuotationsDTO.forEach((pq) -> {
				LOGGER.debug(method + "Converting: " + pq.toString() + " to entity");
				PriceQuotation priceQuotation = this.convertDtoToEntity(pq);

				LOGGER.debug(method + "Saving: " + priceQuotation.toString());
				this.priceQuotationDAO.save(priceQuotation);

				priceQuotations.add(priceQuotation);
			});
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		}

		LOGGER.debug(method + "END");
		return priceQuotations;
	}

	/**
	 * Método responsável por converter um objeto {@link PriceQuotationDTO} para entidade PriceQuotation.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param {@link PriceQuotationDTO}
	 * @return
	 */
	public PriceQuotation convertDtoToEntity(PriceQuotationDTO priceQuotationDTO) {
		String method = "[ PriceQuotationService.convertDtoToEntity ] - ";
		LOGGER.debug(method + "BEGIN");

		PriceQuotation priceQuotation = new PriceQuotation();
		try {
			LOGGER.debug(method + "Loading PriceQuotation");
			priceQuotation.setLink(priceQuotationDTO.getLink());
			priceQuotation.setUnitValue(priceQuotationDTO.getUnitValue());
			priceQuotation.setPurchaseItem(priceQuotationDTO.getPurchaseItem());
			priceQuotation.setAmount(priceQuotationDTO.getAmount());
			priceQuotation.setTotalValue(priceQuotationDTO.getTotalValue());
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		}

		LOGGER.debug(method + "END... Returning: " + priceQuotation.toString());
		return priceQuotation;
	}

}
