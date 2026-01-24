package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.dto.request.ProductCreateRequest;
import com.example.demo.dto.request.ProductPatchRequest;
import com.example.demo.dto.request.ProductUpdateRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	private String name;

	@Column(length = 500)
	private String description;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal price;

	@Column(nullable = false, unique = true, length = 50)
	private String sku;

	@Column(nullable = false)
	private Boolean active;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@Version
	private Long version;

	// --- JPA lifecycle hooks ---
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.active = this.active != null ? this.active : true;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public Product() {
	}

	public Product(String name, String sku, BigDecimal price, Integer quantity) {
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.quantity = quantity;
		this.active = Boolean.TRUE;
	}

	public Product(ProductCreateRequest request) {
		this.name = request.name();
		this.description = request.description();
		this.sku = request.sku();
		this.price = request.price();
		this.quantity = request.quantity();
		this.active = request.active();
	}

	public void update(ProductUpdateRequest request) {
		this.name = request.name();
		this.description = request.description();
		this.sku = request.sku();
		this.price = request.price();
		this.quantity = request.quantity();
		this.active = request.active();
	}

	public void patch(ProductPatchRequest request) {
		if (request.name() != null) {
			this.name = request.name();
		}
		if (request.description() != null) {
			this.description = request.description();
		}
		if (request.quantity() != null) {
			this.quantity = request.quantity();
		}
		if (request.price() != null) {
			this.price = request.price();
		}
		if (request.sku() != null) {
			this.sku = request.sku();
		}
		if (request.active() != null) {
			this.active = request.active();
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getSku() {
		return sku;
	}

	public Boolean getActive() {
		return active;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
