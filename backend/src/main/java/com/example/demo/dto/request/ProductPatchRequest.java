package com.example.demo.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductPatchRequest(

		@Size(min = 2, max = 120) String name,

		@Size(max = 500) String description,

		@PositiveOrZero Integer quantity,

		@DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 10, fraction = 2) BigDecimal price,

		@Size(max = 50) String sku,

		Boolean active,

		@NotNull Long version

) {
}
