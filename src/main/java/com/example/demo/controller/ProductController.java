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

import com.example.demo.dto.request.ProductCreateDTO;
import com.example.demo.dto.request.ProductPatchDTO;
import com.example.demo.dto.request.ProductUpdateDTO;
import com.example.demo.dto.response.ProductDTO;
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
	public ResponseEntity<List<ProductDTO>> findAllProducts() {
		return ResponseEntity.ok(productService.findAllProducts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.findById(id));
	}

	@PostMapping
	public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductCreateDTO dto) {
		ProductDTO created = productService.createProduct(dto);
		return ResponseEntity.created(URI.create("/products/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO dto) {
		return ResponseEntity.ok(productService.updateProduct(id, dto));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		productService.deleteProduct(id);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ProductDTO> patch(@PathVariable Long id, @RequestBody ProductPatchDTO dto) {
		return ResponseEntity.ok(productService.patchProduct(id, dto));
	}

}
