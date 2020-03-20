package br.com.brazilcode.cb.purchase.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Classe de Data Transfer Object para PriceQuotation.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 7 de mar de 2020 15:40:53
 * @version 1.1
 */
public class PriceQuotationDTO {

	@ApiModelProperty(value = "Purchase link URL")
	private String link;

	@ApiModelProperty(value = "Product's unit value")
	private double unitValue;

	@ApiModelProperty(value = "Quotation's observation")
	private String observation;

	@ApiModelProperty(value = "Product's amount")
	private int amount;

	@ApiModelProperty(value = "Product's total value = unit value * amount")
	private double totalValue;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public double getUnitValue() {
		return unitValue;
	}

	public void setUnitValue(double unitValue) {
		this.unitValue = unitValue;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

}
