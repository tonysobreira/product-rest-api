package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.ProductCreateRequest;
import com.example.demo.dto.request.ProductPatchRequest;
import com.example.demo.dto.request.ProductUpdateRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> findAllProducts() {
		return ResponseEntity.ok(productService.findAllProducts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.findById(id));
	}

	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request) {
		ProductResponse created = productService.createProduct(request);
		return ResponseEntity.created(URI.create("/products/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> update(@PathVariable Long id,
			@Valid @RequestBody ProductUpdateRequest request) {
		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		productService.deleteProduct(id);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ProductResponse> patch(@PathVariable Long id, @RequestBody ProductPatchRequest request) {
		return ResponseEntity.ok(productService.patchProduct(id, request));
	}

}
