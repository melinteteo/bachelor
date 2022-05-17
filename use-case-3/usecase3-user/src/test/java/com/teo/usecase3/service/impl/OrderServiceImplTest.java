package com.teo.usecase3.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.teo.usecase3.dto.OrderDto;
import com.teo.usecase3.dto.OrderItemDto;
import com.teo.usecase3.dto.OrderItemRequest;
import com.teo.usecase3.dto.OrderRequest;
import com.teo.usecase3.dto.bankclient.AccountDto;
import com.teo.usecase3.dto.bankclient.TransactionDto;
import com.teo.usecase3.dto.bankclient.TransferRequestDto;
import com.teo.usecase3.dto.productclient.ProductDto;
import com.teo.usecase3.entity.Order;
import com.teo.usecase3.entity.OrderItem;
import com.teo.usecase3.feignclient.BankClient;
import com.teo.usecase3.feignclient.ProductClient;
import com.teo.usecase3.repository.OrderItemRepository;
import com.teo.usecase3.repository.OrderRepository;

@ExtendWith(SpringExtension.class)
class OrderServiceImplTest {
	
	@InjectMocks
	OrderServiceImpl orderServiceImpl;
	
	@Mock
	OrderRepository orderRepository;
	
	@Mock
	OrderItemRepository orderItemRepository;
	
	@Mock
	ProductClient productClient;
	
	@Mock
	BankClient bankClient;
	
	@Test
	void testFindAll() {
		List<Order> orderList = new ArrayList<>();
		Order order = new Order();
		order.setId(1L);
		order.setTransactionId(2L);
		order.setUserId(10L);
		order.setFromAccount("111111");
		order.setTotalPrice(100);
		order.setDateTime(LocalDateTime.now());
		
		OrderItem orderItem = new OrderItem();
		orderItem.setId(5L);
		order.setItems(Set.of(orderItem));
		orderList.add(order);
		
		Mockito.when(orderRepository.findAll()).thenReturn(orderList);
		List<OrderDto> returnedList = orderServiceImpl.findAll();
		
		assertNotNull(returnedList);
		assertEquals(orderList.size(), returnedList.size());
		assertEquals(orderList.get(0).getId(), returnedList.get(0).getId());
		assertEquals(orderList.get(0).getUserId(), returnedList.get(0).getUserId());
		assertEquals(orderList.get(0).getTransactionId(), returnedList.get(0).getTransactionId());
		assertEquals(orderList.get(0).getFromAccount(), returnedList.get(0).getFromAccount());
		assertEquals(orderList.get(0).getTotalPrice(), returnedList.get(0).getTotalPrice());
		assertEquals(orderList.get(0).getDateTime(), returnedList.get(0).getDateTime());
		assertEquals(orderList.get(0).getItems().size(), returnedList.get(0).getItems().size());
		verify(orderRepository).findAll();
	}
	
	@Test
	void testFindByIdFound() {
		Order order = new Order();
		order.setId(1L);
		order.setTransactionId(2L);
		order.setUserId(10L);
		order.setFromAccount("111111");
		order.setTotalPrice(100);
		order.setDateTime(LocalDateTime.now());
		
		OrderItem orderItem = new OrderItem();
		orderItem.setId(5L);
		order.setItems(Set.of(orderItem));
		
		Mockito.when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
		OrderDto returnedOrder = orderServiceImpl.findById(order.getId());
		
		assertNotNull(returnedOrder);
		assertEquals(order.getId(), returnedOrder.getId());
		assertEquals(order.getUserId(), returnedOrder.getUserId());
		assertEquals(order.getTransactionId(), returnedOrder.getTransactionId());
		assertEquals(order.getFromAccount(), returnedOrder.getFromAccount());
		assertEquals(order.getTotalPrice(), returnedOrder.getTotalPrice());
		assertEquals(order.getDateTime(), returnedOrder.getDateTime());
		assertEquals(order.getItems().size(), returnedOrder.getItems().size());
		verify(orderRepository).findById(order.getId());
	}
	
	@Test
	void testFindByIdNotFound() {
		Order order = new Order();
		order.setId(1L);
		order.setTransactionId(2L);
		order.setUserId(10L);
		order.setFromAccount("111111");
		order.setTotalPrice(100);
		order.setDateTime(LocalDateTime.now());
		
		OrderItem orderItem = new OrderItem();
		orderItem.setId(5L);
		order.setItems(Set.of(orderItem));
		
		Mockito.when(orderRepository.findById(order.getId())).thenReturn(Optional.ofNullable(null));
		OrderDto returnedOrder = orderServiceImpl.findById(order.getId());
		
		assertNull(returnedOrder);
		verify(orderRepository).findById(order.getId());
	}
	
	@Test
	void testSave() {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(1L);
		orderDto.setTransactionId(2L);
		orderDto.setUserId(10L);
		orderDto.setFromAccount("111111");
		orderDto.setTotalPrice(100);
		orderDto.setDateTime(LocalDateTime.now());
		
		OrderItemDto orderItemDto = new OrderItemDto();
		orderItemDto.setId(5L);
		ProductDto productDto = new ProductDto();
		productDto.setId(4L);
		orderItemDto.setProduct(productDto);
		orderDto.setItems(Set.of(orderItemDto));
		
		Mockito.when(orderRepository.save(Mockito.any(Order.class))).then(AdditionalAnswers.returnsFirstArg());
		OrderDto returnedOrderDto = orderServiceImpl.save(orderDto);
		
		assertNotNull(returnedOrderDto);
		assertEquals(orderDto.getId(), returnedOrderDto.getId());
		assertEquals(orderDto.getUserId(), returnedOrderDto.getUserId());
		assertEquals(orderDto.getTransactionId(), returnedOrderDto.getTransactionId());
		assertEquals(orderDto.getFromAccount(), returnedOrderDto.getFromAccount());
		assertEquals(orderDto.getTotalPrice(), returnedOrderDto.getTotalPrice());
		assertEquals(orderDto.getDateTime(), returnedOrderDto.getDateTime());
		assertEquals(orderDto.getItems().size(), returnedOrderDto.getItems().size());
		verify(orderRepository).save(Mockito.any(Order.class));
	}
	
	@Test 
	void testDeleteById() {
		orderServiceImpl.deleteById(1L);
		
		verify(orderRepository).deleteById(1L);
	}
	
	@Test
	void testAddOrderSuccess() {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setFromAccountNumber("111111");
		orderRequest.setUserId(1L);
		
		List<OrderItemRequest> itemRequestList = new ArrayList<>();
		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setProductId(1L);
		itemRequest.setNumberOfPieces(2);
		itemRequestList.add(itemRequest);
		orderRequest.setItems(itemRequestList);
		
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(itemRequest.getProductId());
		productDto.setPrice(10);
		productDtoList.add(productDto);
		
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setId(100L);
		transactionDto.setValue(itemRequest.getNumberOfPieces()*productDto.getPrice());
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountNumber(orderRequest.getFromAccountNumber());
		transactionDto.setFromAccount(accountDto);
		
		Mockito.when(productClient.getByIdList(Mockito.anyList())).thenReturn(productDtoList);
		Mockito.when(bankClient.transferFund(Mockito.any())).thenReturn(transactionDto);
		Mockito.when(orderRepository.save(Mockito.any(Order.class))).then(AdditionalAnswers.returnsFirstArg());
		ReflectionTestUtils.setField(orderServiceImpl, "storeAccountNumber", "222222");
		
		OrderDto orderDto = orderServiceImpl.addOrder(orderRequest);
		
		assertNotNull(orderDto);
		assertEquals(orderRequest.getFromAccountNumber(), orderDto.getFromAccount());
		assertEquals(orderRequest.getUserId(), orderDto.getUserId());
		assertEquals(transactionDto.getValue(), orderDto.getTotalPrice());
		assertEquals(transactionDto.getId(), orderDto.getTransactionId());
		assertEquals(itemRequestList.size(), orderDto.getItems().size());
		verify(productClient).getByIdList(Mockito.anyList());
		verify(orderRepository, times(2)).save(Mockito.any(Order.class));
		verify(bankClient).transferFund(Mockito.any(TransferRequestDto.class));
	}
	
	@Test
	void testAddOrderNullToAccountNumber() {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setFromAccountNumber("111111");
		orderRequest.setUserId(1L);
		
		List<OrderItemRequest> itemRequestList = new ArrayList<>();
		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setProductId(1L);
		itemRequest.setNumberOfPieces(2);
		itemRequestList.add(itemRequest);
		orderRequest.setItems(itemRequestList);
		
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(itemRequest.getProductId());
		productDto.setPrice(10);
		productDtoList.add(productDto);
		
		Mockito.when(productClient.getByIdList(Mockito.anyList())).thenReturn(productDtoList);
		
		assertThrows(IllegalStateException.class, () -> orderServiceImpl.addOrder(orderRequest));
	}
	
	@Test
	void testAddOrderWrongSizeToAccountNumber() {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setFromAccountNumber("111111");
		orderRequest.setUserId(1L);
		
		List<OrderItemRequest> itemRequestList = new ArrayList<>();
		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setProductId(1L);
		itemRequest.setNumberOfPieces(2);
		itemRequestList.add(itemRequest);
		orderRequest.setItems(itemRequestList);
		
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(itemRequest.getProductId());
		productDto.setPrice(10);
		productDtoList.add(productDto);
		
		Mockito.when(productClient.getByIdList(Mockito.anyList())).thenReturn(productDtoList);
		ReflectionTestUtils.setField(orderServiceImpl, "storeAccountNumber", "222");
		
		assertThrows(IllegalStateException.class, () -> orderServiceImpl.addOrder(orderRequest));
	}
	
	
	@Test
	void testFindAllByUserIdSuccess() {
		List<Order> orderList = new ArrayList<>();
		Order order = new Order();
		order.setId(1L);
		order.setFromAccount("2222222");
		order.setTotalPrice(10);
		order.setTransactionId(2L);
		order.setUserId(10L);
		
		OrderItem orderItem = new OrderItem();
		orderItem.setId(1L);
		orderItem.setProductId(5L);
		orderItem.setQuantity(1);
		order.addItem(orderItem);
		orderList.add(order);
		
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDto productDto = new ProductDto();
		productDto.setId(orderItem.getProductId());
		productDto.setPrice(10);
		productDtoList.add(productDto);
		
		Mockito.when(orderRepository.findByUserId(order.getUserId())).thenReturn(orderList);
		Mockito.when(productClient.getByIdList(Mockito.anyList())).thenReturn(productDtoList);
		List<OrderDto> returnedList = orderServiceImpl.findAllByUserId(order.getUserId());
		
		assertNotNull(returnedList);
		assertEquals(orderList.size(), returnedList.size());
		assertEquals(order.getId(), returnedList.get(0).getId());
		assertEquals(order.getFromAccount(), returnedList.get(0).getFromAccount());
		assertEquals(order.getTotalPrice(), returnedList.get(0).getTotalPrice());
		assertEquals(order.getTransactionId(), returnedList.get(0).getTransactionId());
		assertEquals(order.getUserId(), returnedList.get(0).getUserId());
		assertEquals(order.getItems().size(), returnedList.get(0).getItems().size());
		verify(orderRepository).findByUserId(order.getUserId());
		verify(productClient).getByIdList(Mockito.anyList());
	}
	
	@Test
	void testFindAllByUserIdNoOrders() {
		List<Order> orderList = new ArrayList<>();
		
		Mockito.when(orderRepository.findByUserId(Mockito.anyLong())).thenReturn(orderList);
		List<OrderDto> returnedList = orderServiceImpl.findAllByUserId(1L);
		
		assertNotNull(returnedList);
		assertEquals(0, returnedList.size());
	}
}
