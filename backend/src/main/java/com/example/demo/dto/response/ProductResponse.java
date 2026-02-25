package com.example.demo.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.model.Product;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "description", "quantity", "price", "sku", "active", "createdAt", "updatedAt",
		"version" })
public record ProductResponse(Long id, String name, String description, Integer quantity, BigDecimal price, String sku,
		Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, Long version) {

	public static ProductResponse from(Product p) {
		return new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getQuantity(), p.getPrice(),
				p.getSku(), p.getActive(), p.getCreatedAt(), p.getUpdatedAt(), p.getVersion());
	}

}
