package com.teo.usecase3.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.teo.usecase3.dto.VendorDto;
import com.teo.usecase3.entity.Product;
import com.teo.usecase3.entity.Vendor;
import com.teo.usecase3.repository.VendorRepository;

@ExtendWith(SpringExtension.class)
class VendorServiceImplTest {
	
	@InjectMocks
	VendorServiceImpl vendorServiceImpl;
	
	@Mock
	VendorRepository vendorRepository;
	
	@Test
	void testFindAll() {
		List<Vendor> vendorList = new ArrayList<>();
		Vendor vendor = new Vendor();
		vendor.setId(1L);
		vendor.setName("Name");
		vendor.setDescription("Description");
		
		Product product = new Product();
		product.setId(2L);
		vendor.setProducts(Set.of(product));
		vendorList.add(vendor);
		
		Mockito.when(vendorRepository.findAll()).thenReturn(vendorList);
		List<VendorDto> returnedList = vendorServiceImpl.findAll();
		
		assertNotNull(returnedList);
		assertEquals(vendorList.size(), returnedList.size());
		assertEquals(vendorList.get(0).getId(), returnedList.get(0).getId());
		assertEquals(vendorList.get(0).getName(), returnedList.get(0).getName());
		assertEquals(vendorList.get(0).getDescription(), returnedList.get(0).getDescription());
		assertEquals(vendorList.get(0).getProducts().size(), returnedList.get(0).getProducts().size());
		verify(vendorRepository).findAll();
	}
	
	@Test
	void testFindByIdFound() {
		Vendor vendor = new Vendor();
		vendor.setId(1L);
		vendor.setName("Name");
		vendor.setDescription("Description");
		
		Product product = new Product();
		product.setId(2L);
		vendor.setProducts(Set.of(product));
		
		Mockito.when(vendorRepository.findById(vendor.getId())).thenReturn(Optional.of(vendor));
		VendorDto returnedVendorDto = vendorServiceImpl.findById(vendor.getId());
		
		assertNotNull(returnedVendorDto);
		assertEquals(vendor.getId(), returnedVendorDto.getId());
		assertEquals(vendor.getName(), returnedVendorDto.getName());
		assertEquals(vendor.getDescription(), returnedVendorDto.getDescription());
		assertEquals(vendor.getProducts().size(), returnedVendorDto.getProducts().size());
		verify(vendorRepository).findById(vendor.getId());
	}
	
	@Test
	void testFindByIdNotFound() {
		Vendor vendor = new Vendor();
		vendor.setId(1L);
		vendor.setName("Name");
		vendor.setDescription("Description");
		
		Product product = new Product();
		product.setId(2L);
		vendor.setProducts(Set.of(product));
		
		Mockito.when(vendorRepository.findById(vendor.getId())).thenReturn(Optional.ofNullable(null));
		VendorDto returnedVendorDto = vendorServiceImpl.findById(vendor.getId());

		
		assertNull(returnedVendorDto);		
	}
	
	@Test
	void testSave() {
		VendorDto vendorDto = new VendorDto();
		vendorDto.setId(1L);
		vendorDto.setName("Name");
		vendorDto.setDescription("Description");
		
		Mockito.when(vendorRepository.save(Mockito.any(Vendor.class))).then(AdditionalAnswers.returnsFirstArg());
		VendorDto returnedVendorDto = vendorServiceImpl.save(vendorDto);
		
		assertNotNull(returnedVendorDto);
		assertEquals(vendorDto.getId(), returnedVendorDto.getId());
		assertEquals(vendorDto.getName(), returnedVendorDto.getName());
		assertEquals(vendorDto.getDescription(), returnedVendorDto.getDescription());
		assertEquals(0, vendorDto.getProducts().size(), returnedVendorDto.getProducts().size());
		
	}
	
	@Test
	void testDeleteById() {
		vendorServiceImpl.deleteById(1L);
		
		verify(vendorRepository).deleteById(1L);
	}
}
