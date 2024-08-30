package com.jazogue.price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class PriceResponseDTO {
	
	private int productId;
	
	private int brandId;
	
	private int priceList;
	
	private LocalDateTime startDate;
	
	private LocalDateTime endDate;
	
	private BigDecimal price;

	public PriceResponseDTO(int productId, int brandId, int priceList, LocalDateTime startDate, LocalDateTime endDate,
			BigDecimal price) {
		this.productId = productId;
		this.brandId = brandId;
		this.priceList = priceList;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
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

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public int getPriceList() {
		return priceList;
	}

	public void setPriceList(int priceList) {
		this.priceList = priceList;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PriceResponseDTO that = (PriceResponseDTO) o;
		return productId == that.productId && brandId == that.brandId && priceList == that.priceList
				&& Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate)
				&& Objects.equals(price, that.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, brandId, priceList, startDate, endDate, price);
	}

	@Override
	public String toString() {
		return "PriceResponseDTO{" + "productId=" + productId + ", brandId=" + brandId + ", priceList=" + priceList
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", price=" + price + '}';
	}
}