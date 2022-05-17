package com.teo.usecase3.batch.vendor;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teo.usecase3.entity.Vendor;
import com.teo.usecase3.repository.VendorRepository;

@Component
public class WriterVendor implements ItemWriter<Vendor> {

	@Autowired
	private VendorRepository vendorRepository;
	
	@Override
	public void write(List<? extends Vendor> items) throws Exception {
		vendorRepository.saveAll(items);
	}
	
}
