package com.teo.usecase3.controller;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teo.usecase3.dto.ProductDto;
import com.teo.usecase3.service.ProductService;
import com.teo.usecase3.utils.AppConstants;
import com.teo.usecase3.utils.Utils;

@RestController
@RequestMapping(AppConstants.PRODUCT_CONTROLLER)
public class ProductController {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("productJob")
	private Job productJob;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping
	public ResponseEntity<List<ProductDto>> getAll() {
		List<ProductDto> returnedList =  productService.findAll();
		
		return new ResponseEntity<>(returnedList, HttpStatus.OK);
	}
	
	@GetMapping(AppConstants.SEARCH_PRODUCT)
	public ResponseEntity<List<ProductDto>> getAllByKeyword(@RequestParam String keyword) {
		List<ProductDto> returnedList = productService.findByNameContaining(keyword);
		
		return new ResponseEntity<>(returnedList, HttpStatus.OK);
	}
	
	@PostMapping(AppConstants.LIST_PRODUCT)
	public ResponseEntity<List<ProductDto>> getByIdList(@RequestBody List<Long> productIdList) {
		if (productIdList.isEmpty()) {
			throw new IllegalArgumentException("List of IDs cannot be empty!");
		}
		
		List<ProductDto> returnedList =  productService.findByIdList(productIdList);
		return new ResponseEntity<>(returnedList, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<String> startBatch(@RequestParam String fileName) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		Utils.checkResourcePath(fileName);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("source", "Vendor Service")
				.addString("fileName", fileName)
				.addString("timestamp", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		
		jobLauncher.run(productJob, jobParameters);
		
		return new ResponseEntity<>("Product job has been launched!", HttpStatus.OK);
		
	}
}
