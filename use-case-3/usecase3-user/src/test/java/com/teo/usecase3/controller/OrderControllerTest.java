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
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.teo.usecase3.dto.OrderDto;
import com.teo.usecase3.dto.OrderItemDto;
import com.teo.usecase3.exception.ControllerExceptions;
import com.teo.usecase3.service.OrderService;
import com.teo.usecase3.utils.AppConstants;

@ExtendWith(SpringExtension.class)
class OrderControllerTest {
	
	MockMvc mockMvc;
	
	@InjectMocks
	OrderController orderController;
	
	@Mock
	OrderService orderService;
	
	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(orderController).setControllerAdvice(new ControllerExceptions())
				.build();
	}
	
	@Test
	void testAddOrder() throws Exception {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(2L);
		orderDto.setTotalPrice(790);
		orderDto.setUserId(1L);
		orderDto.setFromAccount("630403");
		orderDto.setTransactionId(10L);
		
		OrderItemDto orderItemDto = new OrderItemDto();
		orderItemDto.setQuantity(10);
		orderDto.setItems(Set.of(orderItemDto));
		
		Mockito.when(orderService.addOrder(Mockito.any())).thenReturn(orderDto);
		
		
		mockMvc.perform(post(AppConstants.ADD_ORDER)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n"
						+ "    \"userId\": 2,\r\n"
						+ "    \"fromAccountNumber\": \"630403\",\r\n"
						+ "    \"items\": [\r\n"
						+ "        {\r\n"
						+ "            \"numberOfPieces\": 50,\r\n"
						+ "            \"productId\": 3\r\n"
						+ "        }\r\n"
						+ "    ]\r\n"
						+ "}")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(orderDto.getId()))
			.andExpect(jsonPath("$.totalPrice").value(orderDto.getTotalPrice()))
			.andExpect(jsonPath("$.userId").value(orderDto.getUserId()))
			.andExpect(jsonPath("$.fromAccount").value(orderDto.getFromAccount()))
			.andExpect(jsonPath("$.transactionId").value(orderDto.getTransactionId()))
			.andExpect(jsonPath("$.items[0].quantity").value(orderItemDto.getQuantity()));
		
		
	}
	
	@Test
	void testAddOrderMethodArgumentNotValidException() throws Exception {
		mockMvc.perform(post(AppConstants.ADD_ORDER)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n"
						+ "    \"userId\": 2,\r\n"
						+ "    \"fromAccountNumber\": \"630403\",\r\n"
						+ "}")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());	

	}
	
	@Test
	void testAddOrderDataIntegrityViolationException() throws Exception {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(2L);
		orderDto.setTotalPrice(790);
		orderDto.setUserId(1L);
		orderDto.setFromAccount("630403");
		orderDto.setTransactionId(10L);
		Mockito.when(orderService.addOrder(Mockito.any())).thenThrow(DataIntegrityViolationException.class);
		
		
		mockMvc.perform(post(AppConstants.ADD_ORDER)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n"
						+ "    \"userId\": 2,\r\n"
						+ "    \"fromAccountNumber\": \"630403\",\r\n"
						+ "    \"items\": [\r\n"
						+ "        {\r\n"
						+ "            \"numberOfPieces\": 50,\r\n"
						+ "            \"productId\": 3\r\n"
						+ "        }\r\n"
						+ "    ]\r\n"
						+ "}")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
		
	}
	
	@Test
	void getOrdersByUserId() throws Exception {
		List<OrderDto> orderDtoList = new ArrayList<>();
		OrderDto orderDto = new OrderDto();
		orderDto.setId(2L);
		orderDto.setTotalPrice(790);
		orderDto.setUserId(1L);
		orderDto.setFromAccount("630403");
		orderDto.setTransactionId(10L);
		
		OrderItemDto orderItemDto = new OrderItemDto();
		orderItemDto.setQuantity(10);
		orderDto.setItems(Set.of(orderItemDto));
		
		orderDtoList.add(orderDto);
		
		Mockito.when(orderService.findAllByUserId(Mockito.anyLong())).thenReturn(orderDtoList);
		
		mockMvc.perform(get(AppConstants.GET_USER_ORDERS, 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(orderDto.getId()))
			.andExpect(jsonPath("$[0].totalPrice").value(orderDto.getTotalPrice()))
			.andExpect(jsonPath("$[0].userId").value(orderDto.getUserId()))
			.andExpect(jsonPath("$[0].fromAccount").value(orderDto.getFromAccount()))
			.andExpect(jsonPath("$[0].transactionId").value(orderDto.getTransactionId()))
			.andExpect(jsonPath("$[0].items[0].quantity").value(orderItemDto.getQuantity()));
	}
}
