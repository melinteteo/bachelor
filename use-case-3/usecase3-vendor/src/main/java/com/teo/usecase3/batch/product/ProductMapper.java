package com.teo.usecase3.batch.product;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.teo.usecase3.entity.Product;
import com.teo.usecase3.entity.Vendor;

public class ProductMapper implements FieldSetMapper<Product> {

	@Override
	public Product mapFieldSet(FieldSet fieldSet) throws BindException {
		if (fieldSet == null) {
			return null;
		}
		
		Product product = new Product();
		product.setId(fieldSet.readInt("id"));
		product.setName(fieldSet.readString("name"));
		product.setDescription(fieldSet.readString("description"));
		product.setPrice(fieldSet.readInt("price"));
		
		Vendor vendor = new Vendor();
		vendor.setId(fieldSet.readInt("vendor_id"));
		product.setVendor(vendor);
		
		return product;
	}
	
}
