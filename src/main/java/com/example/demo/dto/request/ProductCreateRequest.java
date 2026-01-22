package com.example.demo.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductCreateRequest {

	@NotBlank
	@Size(min = 2, max = 120)
	private String name;

	@Size(max = 500)
	private String description;

	@NotNull
	@Min(0)
	private Integer quantity;

	@NotNull
	@DecimalMin(value = "0.0", inclusive = false)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal price;

	@NotBlank
	@Size(max = 50)
	private String sku;

	@NotNull
	private Boolean active;

	public ProductCreateRequest() {
	}

	public ProductCreateRequest(@NotBlank @Size(min = 2, max = 120) String name, @Size(max = 500) String description,
			@NotNull @Min(0) Integer quantity,
			@NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 10, fraction = 2) BigDecimal price,
			@NotBlank @Size(max = 50) String sku, @NotNull Boolean active) {
		this.name = name;
		this.description = description;
		this.quantity = quantity;
		this.price = price;
		this.sku = sku;
		this.active = active;
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

}
