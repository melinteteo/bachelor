package com.teo.usecase3.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teo.usecase3.dto.ProductDto;
import com.teo.usecase3.dto.VendorDto;
import com.teo.usecase3.entity.Product;
import com.teo.usecase3.entity.Vendor;
import com.teo.usecase3.repository.ProductRepository;
import com.teo.usecase3.repository.VendorRepository;
import com.teo.usecase3.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	public List<ProductDto> findAll() {
		List<Product> productList = productRepository.findAll();
		
		return productList.stream()
				.map(product -> {
					ProductDto productDto = new ProductDto();
					BeanUtils.copyProperties(product, productDto);
					
					VendorDto vendorDto = new VendorDto();
					BeanUtils.copyProperties(product.getVendor(), vendorDto);
					productDto.setVendor(vendorDto);	
					
					return productDto;
				}).collect(Collectors.toList());
	}
	
	public ProductDto findById(Long productId) {
		Optional<Product> productEntity = productRepository.findById(productId);
		
		if (productEntity.isEmpty()) {
			return null;
		}
		
		ProductDto productDto = new ProductDto();
		// unnecessary check
		productEntity.ifPresent(entity -> {
			BeanUtils.copyProperties(entity, productDto);
			
			VendorDto vendorDto = new VendorDto();
			BeanUtils.copyProperties(entity.getVendor(), vendorDto);
			productDto.setVendor(vendorDto);
			
		});
		return productDto;
	}
	
	public ProductDto save(ProductDto productDto) {
		Product product = new Product();
		Vendor vendor = vendorRepository.findById(productDto.getVendor().getId())
							.orElseThrow(() -> new IllegalArgumentException("No vendor with this id!"));
		
		BeanUtils.copyProperties(productDto, product);
		vendor.addProduct(product);
		
		product = productRepository.save(product);
		
		productDto = new ProductDto();
		VendorDto vendorDto = new VendorDto();
		BeanUtils.copyProperties(product, productDto);
		BeanUtils.copyProperties(product.getVendor(), vendorDto);
		productDto.setVendor(vendorDto);
		
		return productDto;
	}
	
	public void deleteById(Long productId) {
		productRepository.deleteById(productId);
	}

	@Override
	public List<ProductDto> findByIdList(List<Long> productIdList) {
		List<Product> productList = productRepository.findAllById(productIdList);
		
		if (productList.size() != productIdList.size()) {
			throw new IllegalArgumentException("One or more IDs is/are incorrect!");
		}
		
		return productList.stream()
				.map(product -> {
					ProductDto productDto = new ProductDto();
					BeanUtils.copyProperties(product, productDto);
					
					VendorDto vendorDto = new VendorDto();
					BeanUtils.copyProperties(product.getVendor(), vendorDto);
					productDto.setVendor(vendorDto);	
					
					return productDto;
				}).collect(Collectors.toList());
	}
	
	@Override
	public List<ProductDto> findByNameContaining(String keyword) {
		List<Product> productList = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
		
		return productList.stream()
				.map(product -> {
					ProductDto productDto = new ProductDto();
					BeanUtils.copyProperties(product, productDto);
					
					VendorDto vendorDto = new VendorDto();
					BeanUtils.copyProperties(product.getVendor(), vendorDto);
					productDto.setVendor(vendorDto);	
					
					return productDto;
				}).collect(Collectors.toList());
	}
	
}
