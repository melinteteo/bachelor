package com.teo.usecase3.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teo.usecase3.dto.ProductDto;
import com.teo.usecase3.dto.VendorDto;
import com.teo.usecase3.entity.Vendor;
import com.teo.usecase3.repository.VendorRepository;
import com.teo.usecase3.service.VendorService;

@Service
public class VendorServiceImpl implements VendorService{
	
	@Autowired
	private VendorRepository vendorRepository;
	
	public List<VendorDto> findAll() {
		List<Vendor> vendorList =  vendorRepository.findAll();
		
		return vendorList.stream().map(vendor -> {
			VendorDto vendorDto = new VendorDto();
			BeanUtils.copyProperties(vendor, vendorDto);
			
			Set<ProductDto> productDtoSet= vendor.getProducts().stream().map(product -> {
				ProductDto productDto = new ProductDto();
				BeanUtils.copyProperties(product, productDto);
				return productDto;
				}).collect(Collectors.toSet());
			
			vendorDto.setProducts(productDtoSet);
			return vendorDto;
		}).collect(Collectors.toList());
	}
	
	public VendorDto findById(Long vendorId) {
		Optional<Vendor> vendorEntity = vendorRepository.findById(vendorId);
		
		if (vendorEntity.isEmpty()) {
			return null;
		}
		
		VendorDto vendorDto = new VendorDto();
		// unnecessary check
		vendorEntity.ifPresent(vendor -> {
			BeanUtils.copyProperties(vendor, vendorDto);
			Set<ProductDto> productDtoSet = vendor.getProducts().stream().map(product -> {
				ProductDto productDto = new ProductDto();
				BeanUtils.copyProperties(product, productDto);
				return productDto;
				}).collect(Collectors.toSet());
			vendorDto.setProducts(productDtoSet);
			});
		
		return vendorDto;
	}
	
	public VendorDto save(VendorDto vendorDto) {
		Vendor vendor = new Vendor();
		BeanUtils.copyProperties(vendorDto, vendor);
		vendor = vendorRepository.save(vendor);
		
		vendorDto = new VendorDto();
		BeanUtils.copyProperties(vendor, vendorDto);
		
		return vendorDto;
	}
	
	public void deleteById(Long vendorId) {
		vendorRepository.deleteById(vendorId);
	}
	
}
