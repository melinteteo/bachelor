package com.teo.usecase3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.teo.usecase3.dto.ProductDto;
import com.teo.usecase3.dto.VendorDto;
import com.teo.usecase3.exception.ControllerExceptions;
import com.teo.usecase3.service.VendorService;
import com.teo.usecase3.utils.AppConstants;
import com.teo.usecase3.utils.Utils;

@ExtendWith(SpringExtension.class)
class VendorControllerTest {
	
	MockMvc mockMvc;
	
	@InjectMocks
	VendorController vendorController;
	

	@Mock
	JobLauncher jobLauncher;
	
	@Mock
	Job productJob;
	
	@Mock
	VendorService vendorService;
	
	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(vendorController).setControllerAdvice(new ControllerExceptions())
				.build();
	}
	
	@Test
	void testGetAll() throws Exception {
		List<VendorDto> vendorDtoList = new ArrayList<>();
		VendorDto vendorDto = new VendorDto();
		vendorDto.setId(1L);
		vendorDto.setName("Name");
		vendorDto.setDescription("Description");
		
		ProductDto productDto = new ProductDto();
		productDto.setName("Name2");
		vendorDto.setProducts(Set.of(productDto));
		vendorDtoList.add(vendorDto);
		
		Mockito.when(vendorService.findAll()).thenReturn(vendorDtoList);
		
		mockMvc.perform(get(AppConstants.VENDOR_CONTROLLER))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(vendorDto.getId()))
			.andExpect(jsonPath("$[0].name").value(vendorDto.getName()))
			.andExpect(jsonPath("$[0].description").value(vendorDto.getDescription()))
			.andExpect(jsonPath("$[0].products[0].name").value("Name2"));
	}
	
	@Test
	void testStartBatch() throws Exception {
		try (MockedStatic<Utils> utilities = Mockito.mockStatic(Utils.class)) {
			mockMvc.perform(post(AppConstants.VENDOR_CONTROLLER)
					.param("fileName", "anyName"))
			.andExpect(status().isOk());
		}
	
	}
	
	@Test
	void testStartBatchInvalidFileNameException() throws Exception {
		
		try (MockedStatic<Utils> utilities = Mockito.mockStatic(Utils.class)) {
			utilities.when(() -> Utils.checkResourcePath(Mockito.anyString())).thenThrow(IllegalArgumentException.class);
			mockMvc.perform(post(AppConstants.VENDOR_CONTROLLER)
					.param("fileName", "anyName"))
			.andExpect(status().isBadRequest());
		}
	
	}
}
