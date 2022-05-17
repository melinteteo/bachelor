package com.teo.usecase3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teo.usecase3.dto.productclient.ProductDto;
import com.teo.usecase3.feignclient.ProductClient;
import com.teo.usecase3.utils.AppConstants;

@RestController
public class ProductController {
	
	@Autowired
	private ProductClient productClient;
	
	@GetMapping(AppConstants.GET_PRODUCTS)
	public ResponseEntity<List<ProductDto>> getProducts(@RequestParam String keyword) {
		List<ProductDto> productsList = productClient.getAllByKeyword(keyword);
		
		return new ResponseEntity<>(productsList, HttpStatus.OK);
	}
}
