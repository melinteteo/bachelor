package com.teo.usecase3.batch.product;

import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teo.usecase3.entity.Product;
import com.teo.usecase3.entity.Vendor;
import com.teo.usecase3.repository.VendorRepository;

@Component
public class ProductProcessor implements ItemProcessor<Product, Product> {
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Override
	public Product process(Product item) throws Exception {
		Optional<Vendor> vendorFromDb = vendorRepository.findById(item.getVendor().getId());
		item.setVendor(vendorFromDb.orElse(null));
		
		if (vendorFromDb.isPresent()) {
			item.setVendor(vendorFromDb.get());
			return item;
		}
		
		return null;
	}
}
