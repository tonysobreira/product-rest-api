package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.dto.request.ProductCreateDTO;
import com.example.demo.dto.request.ProductPatchDTO;
import com.example.demo.dto.request.ProductUpdateDTO;

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

	protected Product() {
	}

	public Product(String name, String sku, BigDecimal price, Integer quantity) {
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.quantity = quantity;
		this.active = Boolean.TRUE;
	}

	public Product(ProductCreateDTO dto) {
		this.name = dto.getName();
		this.description = dto.getDescription();
		this.sku = dto.getSku();
		this.price = dto.getPrice();
		this.quantity = dto.getQuantity();
		this.active = dto.getActive();
	}

	public void update(ProductUpdateDTO dto) {
		this.name = dto.getName();
		this.description = dto.getDescription();
		this.sku = dto.getSku();
		this.price = dto.getPrice();
		this.quantity = dto.getQuantity();
		this.active = dto.getActive();
	}

	public void patch(ProductPatchDTO dto) {
		if (dto.getName() != null) {
			this.name = dto.getName();
		}
		if (dto.getDescription() != null) {
			this.description = dto.getDescription();
		}
		if (dto.getQuantity() != null) {
			this.quantity = dto.getQuantity();
		}
		if (dto.getPrice() != null) {
			this.price = dto.getPrice();
		}
		if (dto.getSku() != null) {
			this.sku = dto.getSku();
		}
		if (dto.getActive() != null) {
			this.active = dto.getActive();
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
