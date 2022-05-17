package com.teo.usecase3.batch.product;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;

import com.teo.usecase3.entity.Product;

public class ReaderProduct extends FlatFileItemReader<Product> {
	
	public ReaderProduct(Resource resource) {
		super();
		
		setResource(resource);
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[] {"id", "vendor_id", "name", "description", "price"});
		lineTokenizer.setDelimiter(",");
		
		DefaultLineMapper<Product> defaultLineMapper = new DefaultLineMapper<>();
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(new ProductMapper());
		setLineMapper(defaultLineMapper);
	}
}
