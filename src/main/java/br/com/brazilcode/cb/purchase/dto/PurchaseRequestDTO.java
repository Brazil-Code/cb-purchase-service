package br.com.brazilcode.cb.purchase.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * Classe de Data Transfer Object para {@link PurchaseRequest}.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 7 de mar de 2020 15:39:30
 * @version 1.0
 */
public class PurchaseRequestDTO {

	@ApiModelProperty(value = "ID creation user")
	private Long createUser;

	@ApiModelProperty(value = "Product's description")
	private String purchaseItem;

	@ApiModelProperty(value = "List of price quotations. Must have 3 to 5 quotations")
	private List<PriceQuotationDTO> priceQuotations = new ArrayList<>();

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public String getPurchaseItem() {
		return purchaseItem;
	}

	public void setPurchaseItem(String purchaseItem) {
		this.purchaseItem = purchaseItem;
	}

	public List<PriceQuotationDTO> getPriceQuotations() {
		return priceQuotations;
	}

	public void setPriceQuotations(List<PriceQuotationDTO> priceQuotations) {
		this.priceQuotations = priceQuotations;
	}

}
