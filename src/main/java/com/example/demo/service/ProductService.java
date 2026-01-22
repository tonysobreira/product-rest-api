package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.request.ProductCreateDTO;
import com.example.demo.dto.request.ProductPatchDTO;
import com.example.demo.dto.request.ProductUpdateDTO;
import com.example.demo.dto.response.ProductDTO;
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
	public List<ProductDTO> findAllProducts() {
		return productRepository.findAll().stream().map(ProductDTO::new).toList();
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		return productRepository.findById(id).map(ProductDTO::new).orElseThrow(() -> new ProductNotFoundException(id));
	}

	@Transactional
	public ProductDTO createProduct(ProductCreateDTO dto) {
		Product product = new Product(dto);
		return new ProductDTO(productRepository.save(product));
	}

	@Transactional
	public ProductDTO updateProduct(Long id, ProductUpdateDTO dto) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		// Force optimistic locking check
		if (!product.getVersion().equals(dto.getVersion())) {
			throw new org.springframework.dao.OptimisticLockingFailureException("Version mismatch");
		}

		product.setVersion(dto.getVersion());

		product.update(dto);
		return new ProductDTO(product);
	}

	@Transactional
	public void deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ProductNotFoundException(id);
		}
		productRepository.deleteById(id);
	}

	@Transactional
	public ProductDTO patchProduct(Long id, ProductPatchDTO dto) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		// Force optimistic locking check
		if (!product.getVersion().equals(dto.getVersion())) {
			throw new org.springframework.dao.OptimisticLockingFailureException("Version mismatch");
		}

		product.setVersion(dto.getVersion());

		product.patch(dto);
		return new ProductDTO(product);
	}

}
