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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teo.usecase3.dto.VendorDto;
import com.teo.usecase3.service.VendorService;
import com.teo.usecase3.utils.AppConstants;
import com.teo.usecase3.utils.Utils;

@RestController
@RequestMapping(AppConstants.VENDOR_CONTROLLER)
public class VendorController {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("vendorJob")
	private Job vendorJob;
	
	@Autowired
	private VendorService vendorService;
	
	@GetMapping
	public ResponseEntity<List<VendorDto>> getAll() {
		List<VendorDto> returnedList = vendorService.findAll();
		
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
		
		jobLauncher.run(vendorJob, jobParameters);
		
		return new ResponseEntity<>("Vendor job has been launched!", HttpStatus.OK);
	}
}
