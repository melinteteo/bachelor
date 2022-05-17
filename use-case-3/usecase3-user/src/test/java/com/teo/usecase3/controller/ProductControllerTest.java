package com.teo.usecase3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.teo.usecase3.dto.productclient.ProductDto;
import com.teo.usecase3.dto.productclient.VendorDto;
import com.teo.usecase3.exception.ControllerExceptions;
import com.teo.usecase3.feignclient.ProductClient;
import com.teo.usecase3.utils.AppConstants;

import feign.FeignException;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {
	
	MockMvc mockMvc;
	
	@InjectMocks
	ProductController productController;
	
	@Mock
	ProductClient productClient;
	
	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(productController).setControllerAdvice(new ControllerExceptions())
				.build();
	}
	
	@Test
	void testGetProductsSuccess() throws Exception {
		List<ProductDto> productsList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(1L);
		productDto.setName("Name");
		productDto.setPrice(100);
		productDto.setDescription("Description");
		VendorDto vendorDto = new VendorDto();
		vendorDto.setName("Name2");
		productDto.setVendor(vendorDto);
		productsList.add(productDto);
		
		Mockito.when(productClient.getAllByKeyword(Mockito.anyString())).thenReturn(productsList);
		
		mockMvc.perform(get(AppConstants.GET_PRODUCTS)
				.param("keyword", "food"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(productDto.getId()))
			.andExpect(jsonPath("$[0].name").value(productDto.getName()))
			.andExpect(jsonPath("$[0].price").value(productDto.getPrice()))
			.andExpect(jsonPath("$[0].description").value(productDto.getDescription()))
			.andExpect(jsonPath("$[0].vendor.name").value(vendorDto.getName()));
		
	}
	
	@Test
	void testGetProductsSuccessExceptionFeign() throws Exception {
		Mockito.when(productClient.getAllByKeyword(Mockito.anyString())).thenThrow(FeignException.class);
		
		mockMvc.perform(get(AppConstants.GET_PRODUCTS)
				.param("keyword", "food"))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void testGetProductsSuccessExceptionParam() throws Exception {
		mockMvc.perform(get(AppConstants.GET_PRODUCTS))
			.andExpect(status().isBadRequest());
	}
	
}
