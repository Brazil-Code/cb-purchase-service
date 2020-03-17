package br.com.brazilcode.cb.purchase.dto;

/**
 * Classe de Data Transfer Object para PriceQuotation.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 7 de mar de 2020 15:40:53
 * @version 1.1
 */
public class PriceQuotationDTO {

	private String link;
	private double unitValue;
	private String observation;
	private int amount;
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
