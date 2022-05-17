package com.teo.usecase3.batch.product;

import java.util.List;
import java.util.Optional;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.teo.usecase3.entity.Product;
import com.teo.usecase3.entity.Vendor;
import com.teo.usecase3.repository.ProductRepository;
import com.teo.usecase3.repository.VendorRepository;

@Component
public class WriterProduct implements ItemWriter<Product> {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private VendorRepository vendorRepository;

	@Override
	@Transactional
	public void write(List<? extends Product> items) throws Exception {
		productRepository.saveAll(items);
		items.forEach(item -> {
			if (item == null) {
				return;
			}
			
			Optional<Vendor> vendor = vendorRepository.findById(item.getVendor().getId());
			vendor.ifPresent(v -> v.addProduct(item));
		});
	}
}
