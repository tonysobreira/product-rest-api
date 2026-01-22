package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.request.ProductCreateRequest;
import com.example.demo.dto.request.ProductPatchRequest;
import com.example.demo.dto.request.ProductUpdateRequest;
import com.example.demo.dto.response.ProductResponse;
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
	public List<ProductResponse> findAllProducts() {
		return productRepository.findAll().stream().map(ProductResponse::new).toList();
	}

	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		return productRepository.findById(id).map(ProductResponse::new)
				.orElseThrow(() -> new ProductNotFoundException(id));
	}

	@Transactional
	public ProductResponse createProduct(ProductCreateRequest request) {
		Product product = new Product(request);
		return new ProductResponse(productRepository.save(product));
	}

	@Transactional
	public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		// Force optimistic locking check
		if (!product.getVersion().equals(request.getVersion())) {
			throw new org.springframework.dao.OptimisticLockingFailureException("Version mismatch");
		}

		product.setVersion(request.getVersion());

		product.update(request);
		return new ProductResponse(product);
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

		// Force optimistic locking check
		if (!product.getVersion().equals(request.getVersion())) {
			throw new org.springframework.dao.OptimisticLockingFailureException("Version mismatch");
		}

		product.setVersion(request.getVersion());

		product.patch(request);
		return new ProductResponse(product);
	}

}
