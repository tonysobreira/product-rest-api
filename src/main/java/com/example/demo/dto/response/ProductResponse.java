package com.example.demo.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.model.Product;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "description", "quantity", "quantity", "price", "sku", "active", "createdAt",
		"updatedAt", "version" })
public class ProductResponse {

	private Long id;

	private String name;

	private String description;

	private Integer quantity;

	private BigDecimal price;

	private String sku;

	private Boolean active;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Long version;

	public ProductResponse() {
	}

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.quantity = product.getQuantity();
		this.price = product.getPrice();
		this.sku = product.getSku();
		this.active = product.getActive();
		this.createdAt = product.getCreatedAt();
		this.updatedAt = product.getUpdatedAt();
		this.version = product.getVersion();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
