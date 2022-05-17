package com.teo.usecase3.batch.vendor;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;

import com.teo.usecase3.entity.Vendor;

public class ReaderVendor extends FlatFileItemReader<Vendor>{
	
	public ReaderVendor(Resource resource) {
		super();
		
		setResource(resource);
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[] {"id", "name", "description"});
		lineTokenizer.setDelimiter(",");
		
		BeanWrapperFieldSetMapper<Vendor> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Vendor.class);
		
		DefaultLineMapper<Vendor> defaultLineMapper = new DefaultLineMapper<>();
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);
		setLineMapper(defaultLineMapper);
	}
}
