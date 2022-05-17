package com.teo.usecase3.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.teo.usecase3.dto.ProductDto;
import com.teo.usecase3.dto.VendorDto;
import com.teo.usecase3.entity.Product;
import com.teo.usecase3.entity.Vendor;
import com.teo.usecase3.repository.ProductRepository;
import com.teo.usecase3.repository.VendorRepository;

@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {
	
	@InjectMocks
	ProductServiceImpl productServiceImpl;
	
	@Mock
	ProductRepository productRepository;
	
	@Mock
	VendorRepository vendorRepository;
	
	@Test
	void testFindAllTest() {
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		product.setId(1L);
		product.setName("Name");
		product.setDescription("Description");
		product.setPrice(100);
		
		Vendor vendor = new Vendor();
		vendor.setId(2L);
		vendor.addProduct(product);
		productList.add(product);
		
		Mockito.when(productRepository.findAll()).thenReturn(productList);
		List<ProductDto> returnedList = productServiceImpl.findAll();
		
		assertNotNull(returnedList);
		assertEquals(productList.size(), returnedList.size());
		assertEquals(productList.get(0).getName(), returnedList.get(0).getName());
		assertEquals(productList.get(0).getDescription(), returnedList.get(0).getDescription());
		assertEquals(productList.get(0).getPrice(), returnedList.get(0).getPrice());
		assertEquals(productList.get(0).getVendor().getId(), returnedList.get(0).getVendor().getId());
		verify(productRepository).findAll();
	}
	
	@Test
	void testFindByIdFound() {
		Product product = new Product();
		product.setId(1L);
		product.setName("Name");
		product.setDescription("Description");
		product.setPrice(100);
		
		Vendor vendor = new Vendor();
		vendor.setId(2L);
		vendor.addProduct(product);
		
		Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		ProductDto returnedProductDto = productServiceImpl.findById(product.getId());
		
		assertNotNull(returnedProductDto);
		assertEquals(product.getId(), returnedProductDto.getId());
		assertEquals(product.getName(), returnedProductDto.getName());
		assertEquals(product.getDescription(), returnedProductDto.getDescription());
		assertEquals(product.getPrice(), returnedProductDto.getPrice());
		assertEquals(product.getVendor().getId(), returnedProductDto.getVendor().getId());
		verify(productRepository).findById(product.getId());
	}
	
	@Test
	void testFindByIdNotFound() {
		Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		ProductDto returnedProductDto = productServiceImpl.findById(1L);
		
		assertNull(returnedProductDto);
		verify(productRepository).findById(Mockito.anyLong());
	}
	
	@Test
	void testSave() {
		ProductDto productDto = new ProductDto();
		productDto.setId(1L);
		productDto.setName("Name");
		productDto.setDescription("Description");
		productDto.setPrice(100);
		
		VendorDto vendorDto = new VendorDto();
		vendorDto.setId(2L);
		productDto.setVendor(vendorDto);
		
		Vendor vendor = new Vendor();
		vendor.setId(vendorDto.getId());
		vendor.setDescription("Description2");
		vendor.setName("Name2");
		
		Mockito.when(vendorRepository.findById(vendorDto.getId())).thenReturn(Optional.of(vendor));
		Mockito.when(productRepository.save(Mockito.any(Product.class))).then(AdditionalAnswers.returnsFirstArg());
		ProductDto returnedProductDto = productServiceImpl.save(productDto);
		
		assertNotNull(returnedProductDto);
		assertEquals(productDto.getId(), returnedProductDto.getId());
		assertEquals(productDto.getName(), returnedProductDto.getName());
		assertEquals(productDto.getDescription(), returnedProductDto.getDescription());
		assertEquals(productDto.getPrice(), returnedProductDto.getPrice());
		assertEquals(productDto.getVendor().getId(), returnedProductDto.getVendor().getId());
		verify(vendorRepository).findById(vendorDto.getId());
		verify(productRepository).save(Mockito.any(Product.class));
		
	}
	
	@Test
	void testSaveInvalidVendorIdException() {
		ProductDto productDto = new ProductDto();
		productDto.setId(1L);
		productDto.setName("Name");
		productDto.setDescription("Description");
		productDto.setPrice(100);
		
		VendorDto vendorDto = new VendorDto();
		vendorDto.setId(2L);
		productDto.setVendor(vendorDto);
	
		
		Mockito.when(vendorRepository.findById(vendorDto.getId())).thenReturn(Optional.ofNullable(null));
		Mockito.when(productRepository.save(Mockito.any(Product.class))).then(AdditionalAnswers.returnsFirstArg());
		
		assertThrows(IllegalArgumentException.class, () -> productServiceImpl.save(productDto));
		
	}
	
	@Test
	void testDeleteById() {
		productServiceImpl.deleteById(1L);
		
		verify(productRepository).deleteById(1L);
	}
	
	@Test
	void testFindByIdList() {
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		product.setId(1L);
		product.setName("Name");
		product.setDescription("Description");
		product.setPrice(100);
		
		Vendor vendor = new Vendor();
		vendor.setId(2L);
		vendor.addProduct(product);
		productList.add(product);
		
		Mockito.when(productRepository.findAllById(Mockito.anyList())).thenReturn(productList);
		List<ProductDto> returnedProductDto = productServiceImpl.findByIdList(List.of(1L));
		
		assertNotNull(returnedProductDto);
		assertEquals(productList.size(), returnedProductDto.size());
		assertEquals(productList.get(0).getId(), returnedProductDto.get(0).getId());
		assertEquals(productList.get(0).getName(), returnedProductDto.get(0).getName());
		assertEquals(productList.get(0).getDescription(), returnedProductDto.get(0).getDescription());
		assertEquals(productList.get(0).getPrice(), returnedProductDto.get(0).getPrice());
		assertEquals(productList.get(0).getVendor().getId(), returnedProductDto.get(0).getVendor().getId());
		verify(productRepository).findAllById(Mockito.anyList());
	}
	
	@Test
	void testFindByIdListInvalidInputListException() {
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		product.setId(1L);
		product.setName("Name");
		product.setDescription("Description");
		product.setPrice(100);
		
		Vendor vendor = new Vendor();
		vendor.setId(2L);
		vendor.addProduct(product);
		productList.add(product);
		
		Mockito.when(productRepository.findAllById(Mockito.anyList())).thenReturn(productList);
		
		List<Long> list = new ArrayList<>(); 
		assertThrows(IllegalArgumentException.class, () -> productServiceImpl.findByIdList(list));
	}
	
	@Test
	void testFindByNameContaining() {
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		product.setId(1L);
		product.setName("Name");
		product.setDescription("Description");
		product.setPrice(100);
		
		Vendor vendor = new Vendor();
		vendor.setId(2L);
		vendor.addProduct(product);
		productList.add(product);
		
		Mockito.when(productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(Mockito.anyString(), Mockito.anyString()))
			.thenReturn(productList);
		List<ProductDto> returnedProductDto = productServiceImpl.findByNameContaining("AnyString");
		
		assertNotNull(returnedProductDto);
		assertEquals(productList.size(), returnedProductDto.size());
		assertEquals(productList.get(0).getId(), returnedProductDto.get(0).getId());
		assertEquals(productList.get(0).getName(), returnedProductDto.get(0).getName());
		assertEquals(productList.get(0).getDescription(), returnedProductDto.get(0).getDescription());
		assertEquals(productList.get(0).getPrice(), returnedProductDto.get(0).getPrice());
		assertEquals(productList.get(0).getVendor().getId(), returnedProductDto.get(0).getVendor().getId());
		verify(productRepository).findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(Mockito.anyString(), Mockito.anyString());
	}
}
