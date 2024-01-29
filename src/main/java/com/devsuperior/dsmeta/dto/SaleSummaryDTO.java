package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.entities.Sale;

public class SaleSummaryDTO {
	private String sellerName;
	private Double total;

	public SaleSummaryDTO() {
		super();
	}

	public SaleSummaryDTO(String sellerName, Double total) {
		super();
		this.sellerName = sellerName;
		this.total = total;
	}

	public SaleSummaryDTO(Sale entity) {
		this.sellerName = entity.getSeller().getName();
		this.total = entity.getAmount();
	}

	public String getSellerName() {
		return sellerName;
	}

	public Double getTotal() {
		return total;
	}

}
