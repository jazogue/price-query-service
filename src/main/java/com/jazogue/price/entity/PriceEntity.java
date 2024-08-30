package com.jazogue.price.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jazogue.price.entity.id.PriceId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRICES")
@IdClass(PriceId.class)
public class PriceEntity {

	@Id
	@Column(name = "PRICE_LIST")
	private int priceList;

	@Id
	@Column(name = "PRODUCT_ID")
	private int productId;

	@Column(name = "BRAND_ID")
	private int brandId;

	@Column(name = "START_DATE")
	private LocalDateTime startDate;

	@Column(name = "END_DATE")
	private LocalDateTime endDate;

	@Column(name = "PRIORITY")
	private int priority;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "CURR")
	private String curr;

	public PriceEntity() {
	}

	public int getPriceList() {
		return priceList;
	}

	public void setPriceList(int priceList) {
		this.priceList = priceList;
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	@Override
	public String toString() {
		return "Price{" + "priceList=" + priceList + ", productId=" + productId + ", brandId=" + brandId
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", priority=" + priority + ", price=" + price
				+ ", curr='" + curr + '\'' + '}';
	}

}
