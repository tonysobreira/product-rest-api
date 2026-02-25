package com.example.demo.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(

		@NotBlank @Size(min = 2, max = 120) String name,

		@Size(max = 500) String description,

		@NotNull @PositiveOrZero Integer quantity,

		@NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 10, fraction = 2) BigDecimal price,

		@NotBlank @Size(max = 50) String sku,

		@NotNull Boolean active

) {
}
