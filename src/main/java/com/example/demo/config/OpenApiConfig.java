package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

//	Swagger UI: http://localhost:8080/swagger-ui.html
//	OpenAPI JSON: http://localhost:8080/v3/api-docs
//	OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml
	@Bean
	public OpenAPI productApi() {
		return new OpenAPI().info(new Info().title("Product API").version("v1").description("CRUD API for products"));
	}

}
