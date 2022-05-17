package com.teo.usecase3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.teo.usecase3.dto.ProductDto;
import com.teo.usecase3.dto.VendorDto;
import com.teo.usecase3.exception.ControllerExceptions;
import com.teo.usecase3.service.ProductService;
import com.teo.usecase3.utils.AppConstants;
import com.teo.usecase3.utils.Utils;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {
	
	MockMvc mockMvc;
	
	@InjectMocks
	ProductController productController;
	
	@Mock
	JobLauncher jobLauncher;
	
	@Mock
	Job productJob;
	
	@Mock
	ProductService productService;
	
	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(productController).setControllerAdvice(new ControllerExceptions())
				.build();
	}
	
	@Test
	void testGetAll() throws Exception {
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(1L);
		productDto.setName("Name");
		productDto.setDescription("Description");
		productDto.setPrice(100);
		
		VendorDto vendorDto = new VendorDto();
		vendorDto.setName("Name2");
		productDto.setVendor(vendorDto);
		productDtoList.add(productDto);
		
		Mockito.when(productService.findAll()).thenReturn(productDtoList);
		
		mockMvc.perform(get(AppConstants.PRODUCT_CONTROLLER))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(productDto.getId()))
			.andExpect(jsonPath("$[0].name").value(productDto.getName()))
			.andExpect(jsonPath("$[0].description").value(productDto.getDescription()))
			.andExpect(jsonPath("$[0].price").value(productDto.getPrice()))
			.andExpect(jsonPath("$[0].vendor.name").value(productDto.getVendor().getName()));
	}
	
	@Test
	void testGetAllByKeyword() throws Exception {
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(1L);
		productDto.setName("Name");
		productDto.setDescription("Description");
		productDto.setPrice(100);
		
		VendorDto vendorDto = new VendorDto();
		vendorDto.setName("Name2");
		productDto.setVendor(vendorDto);
		productDtoList.add(productDto);
		
		Mockito.when(productService.findByNameContaining(Mockito.anyString())).thenReturn(productDtoList);
		
		mockMvc.perform(get(AppConstants.PRODUCT_CONTROLLER + AppConstants.SEARCH_PRODUCT)
				.param("keyword", "AnyString"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(productDto.getId()))
		.andExpect(jsonPath("$[0].name").value(productDto.getName()))
		.andExpect(jsonPath("$[0].description").value(productDto.getDescription()))
		.andExpect(jsonPath("$[0].price").value(productDto.getPrice()))
		.andExpect(jsonPath("$[0].vendor.name").value(productDto.getVendor().getName()));
	}
	
	@Test
	void testGetAllByKeywordRequestParamException() throws Exception {
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(1L);
		productDto.setName("Name");
		productDto.setDescription("Description");
		productDto.setPrice(100);
		
		VendorDto vendorDto = new VendorDto();
		vendorDto.setName("Name2");
		productDto.setVendor(vendorDto);
		productDtoList.add(productDto);
		
		Mockito.when(productService.findByNameContaining(Mockito.anyString())).thenReturn(productDtoList);
		
		mockMvc.perform(get(AppConstants.PRODUCT_CONTROLLER + AppConstants.SEARCH_PRODUCT))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void testGetByIdList() throws Exception {
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(1L);
		productDto.setName("Name");
		productDto.setDescription("Description");
		productDto.setPrice(100);
		
		VendorDto vendorDto = new VendorDto();
		vendorDto.setName("Name2");
		productDto.setVendor(vendorDto);
		productDtoList.add(productDto);
		
		Mockito.when(productService.findByIdList(Mockito.anyList())).thenReturn(productDtoList);
		
		mockMvc.perform(post(AppConstants.PRODUCT_CONTROLLER + AppConstants.LIST_PRODUCT)
				.contentType(MediaType.APPLICATION_JSON)
				.content("[1,2]")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(productDto.getId()))
		.andExpect(jsonPath("$[0].name").value(productDto.getName()))
		.andExpect(jsonPath("$[0].description").value(productDto.getDescription()))
		.andExpect(jsonPath("$[0].price").value(productDto.getPrice()))
		.andExpect(jsonPath("$[0].vendor.name").value(productDto.getVendor().getName()));
	}
	
	@Test
	void testGetByIdListNoRequestBodyException() throws Exception {
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(1L);
		productDto.setName("Name");
		productDto.setDescription("Description");
		productDto.setPrice(100);
		
		VendorDto vendorDto = new VendorDto();
		vendorDto.setName("Name2");
		productDto.setVendor(vendorDto);
		productDtoList.add(productDto);
		
		Mockito.when(productService.findByIdList(Mockito.anyList())).thenReturn(productDtoList);
		
		mockMvc.perform(post(AppConstants.PRODUCT_CONTROLLER + AppConstants.LIST_PRODUCT)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void testGetByIdListReturnedListEmptyException() throws Exception {
		List<ProductDto> productDtoList = new ArrayList<>();
		
		Mockito.when(productService.findByIdList(Mockito.anyList())).thenReturn(productDtoList);
		
		mockMvc.perform(post(AppConstants.PRODUCT_CONTROLLER + AppConstants.LIST_PRODUCT)
				.contentType(MediaType.APPLICATION_JSON)
				.content("[]")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void testStartBatch() throws Exception {
		try (MockedStatic<Utils> utilities = Mockito.mockStatic(Utils.class)) {
			mockMvc.perform(post(AppConstants.PRODUCT_CONTROLLER)
					.param("fileName", "anyName"))
			.andExpect(status().isOk());
		}
	
	}
	
	@Test
	void testStartBatchInvalidFileNameException() throws Exception {
		
		try (MockedStatic<Utils> utilities = Mockito.mockStatic(Utils.class)) {
			utilities.when(() -> Utils.checkResourcePath(Mockito.anyString())).thenThrow(IllegalArgumentException.class);
			mockMvc.perform(post(AppConstants.PRODUCT_CONTROLLER)
					.param("fileName", "anyName"))
			.andExpect(status().isBadRequest());
		}
	
	}
}
