package com.jazogue.price.dto;

import java.time.LocalDateTime;

public class PriceRequestDTO {

	private LocalDateTime applicationDate;

	private int productId;

	private int brandId;

	public PriceRequestDTO(LocalDateTime applicationDate, int productId, int brandId) {
		super();
		this.applicationDate = applicationDate;
		this.productId = productId;
		this.brandId = brandId;
	}

	public LocalDateTime getApplicationDate() {
		return applicationDate;
	}

	public void setDate(LocalDateTime applicationDate) {
		this.applicationDate = applicationDate;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setPriceList(int brandId) {
		this.brandId = brandId;
	}
}