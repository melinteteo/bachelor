package com.teo.usecase3.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teo.usecase3.dto.OrderDto;
import com.teo.usecase3.dto.OrderRequest;
import com.teo.usecase3.service.OrderService;
import com.teo.usecase3.utils.AppConstants;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping(AppConstants.ADD_ORDER)
	public ResponseEntity<OrderDto> addOrder(@Valid @RequestBody OrderRequest orderRequest) {
		OrderDto orderDto = orderService.addOrder(orderRequest);
		return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
	}
	
	@GetMapping(AppConstants.GET_USER_ORDERS)
	public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable long userId) {
		List<OrderDto> orderDtoList = orderService.findAllByUserId(userId);
		return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
	}
}
