package com.teo.usecase3.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teo.usecase3.dto.OrderDto;
import com.teo.usecase3.dto.OrderItemDto;
import com.teo.usecase3.dto.OrderItemRequest;
import com.teo.usecase3.dto.OrderRequest;
import com.teo.usecase3.dto.bankclient.TransactionDto;
import com.teo.usecase3.dto.bankclient.TransferRequestDto;
import com.teo.usecase3.dto.productclient.ProductDto;
import com.teo.usecase3.entity.Order;
import com.teo.usecase3.entity.OrderItem;
import com.teo.usecase3.feignclient.BankClient;
import com.teo.usecase3.feignclient.ProductClient;
import com.teo.usecase3.repository.OrderItemRepository;
import com.teo.usecase3.repository.OrderRepository;
import com.teo.usecase3.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private ProductClient productClient;
	
	@Autowired
	private BankClient bankClient;
	
	@Value("${store.account.number}")
	private String storeAccountNumber;
	
	@Override
	public List<OrderDto> findAll() {
		List<Order> entityList = orderRepository.findAll();
		
		return entityList.stream()
				.map(entity -> {
					OrderDto dto = new OrderDto();
					BeanUtils.copyProperties(entity, dto);
					
					Set<OrderItemDto> itemDtoSet = entity.getItems().stream().map(item -> {
						OrderItemDto itemDto = new OrderItemDto();
						BeanUtils.copyProperties(item, itemDto);
						
						//Product is not fetched, only added the product id
						ProductDto productDto = new ProductDto();
						productDto.setId(item.getProductId());
						itemDto.setProduct(productDto);
						
						
						return itemDto;
					}).collect(Collectors.toSet());
					
					dto.setItems(itemDtoSet);
					
					return dto;
				}).collect(Collectors.toList());
	}

	@Override
	public OrderDto findById(Long id) {
		Order entity = orderRepository.findById(id).orElse(null);
		
		if(entity == null) {
			return null;
		}
		
		OrderDto dto = new OrderDto();
		BeanUtils.copyProperties(entity, dto);
		
		Set<OrderItemDto> itemDtoSet = entity.getItems().stream().map(item -> {
			OrderItemDto itemDto = new OrderItemDto();
			BeanUtils.copyProperties(item, itemDto);
			
			//Product is not fetched, only added the product id
			ProductDto productDto = new ProductDto();
			productDto.setId(item.getProductId());
			itemDto.setProduct(productDto);
			
			return itemDto;
		}).collect(Collectors.toSet());
		
		dto.setItems(itemDtoSet);
		
		return dto;
	}

	@Override
	public OrderDto save(OrderDto object) {
		Order entity = new Order();
		BeanUtils.copyProperties(object, entity);
		
		object.getItems().forEach(itemDto -> {
				OrderItem item = new OrderItem();
				BeanUtils.copyProperties(itemDto, item);
				item.setProductId(itemDto.getProduct().getId());
				entity.addItem(item);
		});
		
		Order returnedEntity = orderRepository.save(entity);
		BeanUtils.copyProperties(returnedEntity, object);
		
		Set<OrderItemDto> itemDtoSet = returnedEntity.getItems().stream().map(item -> {
			OrderItemDto itemDto = new OrderItemDto();
			BeanUtils.copyProperties(item, itemDto);
			
			//Product is not fetched, only added the product id
			ProductDto productDto = new ProductDto();
			productDto.setId(item.getProductId());
			itemDto.setProduct(productDto);
			
			return itemDto;
		}).collect(Collectors.toSet());
		
		object.setItems(itemDtoSet);
		return object;
	}

	@Override
	public void deleteById(Long id) {
		orderRepository.deleteById(id);
	}

	/*
	 * Used methods
	 */
	@Override
	@Transactional
	public OrderDto addOrder(OrderRequest orderRequest) {
		// Getting information about products (exception will be thrown if any id is wrong)
		List<Long> productIdList = new ArrayList<>(orderRequest.getItems().size());
		orderRequest.getItems().forEach(item -> productIdList.add(item.getProductId()));
		List<ProductDto> products = productClient.getByIdList(productIdList);
		
		//Creating a HashMap from product list
		Map<Long, ProductDto> productMap = new HashMap<>();
		products.forEach(product -> productMap.put(product.getId(), product));
		
		// Checking store account number from properties file
		if (storeAccountNumber == null || storeAccountNumber.length() != 6) {
			throw new IllegalStateException("Please set an appropriate account number in properties file! (store.account.number)");
		}
		
		// Creating the order
		Order order = new Order();
		orderRepository.save(order);
		
		int price = 0;
		for (OrderItemRequest item : orderRequest.getItems()) {
			price += item.getNumberOfPieces() * productMap.get(item.getProductId()).getPrice();
		}
		
		TransferRequestDto transferRequestDto = new TransferRequestDto();
		transferRequestDto.setValue(price);
		transferRequestDto.setFromAccountNumber(orderRequest.getFromAccountNumber());
		transferRequestDto.setToAccountNumber(storeAccountNumber);
		transferRequestDto.setDescription("Payment for order number " + order.getId() + ".");
		
		TransactionDto transactionDto = bankClient.transferFund(transferRequestDto);
		
		order.setDateTime(LocalDateTime.now());
		order.setFromAccount(transactionDto.getFromAccount().getAccountNumber());
		order.setTotalPrice(price);
		order.setTransactionId(transactionDto.getId());
		order.setUserId(orderRequest.getUserId());
		
		for (OrderItemRequest item : orderRequest.getItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setProductId(item.getProductId());
			orderItem.setQuantity(item.getNumberOfPieces());
			order.addItem(orderItem);
			orderItemRepository.save(orderItem);
		}
		
		order = orderRepository.save(order);
		
		// Creating the response DTO
		Set<OrderItemDto> items;
		
		items = order.getItems().stream().map(entity -> {
			OrderItemDto dto = new OrderItemDto();
			dto.setProduct(productMap.get(entity.getProductId()));
			BeanUtils.copyProperties(entity, dto, "product");
			return dto;
		}).collect(Collectors.toSet());
		
		OrderDto orderDto = new OrderDto();
		orderDto.setItems(items);
		BeanUtils.copyProperties(order, orderDto, "items");
		
		return orderDto;
	}

	@Override
	public List<OrderDto> findAllByUserId(long userId) {
		List<Order> orderList = orderRepository.findByUserId(userId);
		
		if (orderList.isEmpty()) {
			return new ArrayList<>();
		}
		
		// Creating a set of unique needed products id in order to fetch from Vendor Service
		Set<Long> productIdSet = new HashSet<>();
		orderList.stream().map(Order::getItems)
			.reduce((set1, set2) -> {
				Set<OrderItem> set = new HashSet<>();
				set.addAll(set1);
				set.addAll(set2);
				return set;
			}).orElseThrow(() -> new IllegalStateException("An error appeared during product loading!"))
			.forEach(orderItem -> productIdSet.add(orderItem.getProductId()));
		
		List<ProductDto> products = productClient.getByIdList(new ArrayList<>(productIdSet));
		
		// Creating a HashMap from product list
		Map<Long, ProductDto> productMap = new HashMap<>();
		products.forEach(product -> productMap.put(product.getId(), product));
		
		
		return orderList.stream().map(entity -> {
			OrderDto dto = new OrderDto();
			BeanUtils.copyProperties(entity, dto);
			
			Set<OrderItemDto> items = entity.getItems().stream()
					.map(itemEntity -> {
						OrderItemDto itemDto = new OrderItemDto();
						BeanUtils.copyProperties(itemEntity, itemDto);
						itemDto.setProduct(productMap.get(itemEntity.getProductId()));
						return itemDto;
					}).collect(Collectors.toSet());
			dto.setItems(items);
			
			return dto;
		}).collect(Collectors.toList());
				
	}
	
}
