package com.example.demo.service;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.request.ProductCreateRequest;
import com.example.demo.dto.request.ProductPatchRequest;
import com.example.demo.dto.request.ProductUpdateRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public Page<ProductResponse> findAllProducts(Pageable pageable) {
		return productRepository.findAll(pageable).map(ProductResponse::from);
	}

	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		return productRepository.findById(id).map(ProductResponse::from)
				.orElseThrow(() -> new ProductNotFoundException(id));
	}

	@Transactional
	public ProductResponse createProduct(ProductCreateRequest request) {
		if (productRepository.existsBySku(request.sku())) {
			throw new ConflictException("SKU already exists: " + request.sku());
		}

		Product product = new Product(request);
		return ProductResponse.from(productRepository.save(product));
	}

	@Transactional
	public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		// only if sku is allowed to change:
		if (!product.getSku().equals(request.sku()) && productRepository.existsBySku(request.sku())) {
			throw new ConflictException("SKU already exists: " + request.sku());
		}

		// Force optimistic locking check
		if (!Objects.equals(product.getVersion(), request.version())) {
			throw new ConflictException(
					"Version mismatch. Current=" + product.getVersion() + ", Provided=" + request.version());
		}

		product.update(request);
		return ProductResponse.from(product);
	}

	@Transactional
	public void deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ProductNotFoundException(id);
		}
		productRepository.deleteById(id);
	}

	@Transactional
	public ProductResponse patchProduct(Long id, ProductPatchRequest request) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		// optimistic check (don’t set version manually)
		if (!Objects.equals(product.getVersion(), request.version())) {
			throw new ConflictException(
					"Version mismatch. Current=" + product.getVersion() + ", Provided=" + request.version());
		}

		product.patch(request);
		return ProductResponse.from(product);
	}

}
