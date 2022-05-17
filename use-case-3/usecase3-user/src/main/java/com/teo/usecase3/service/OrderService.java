package com.teo.usecase3.service;

import java.util.List;

import javax.validation.Valid;

import com.teo.usecase3.dto.OrderDto;
import com.teo.usecase3.dto.OrderRequest;

public interface OrderService extends CrudService<OrderDto, Long> {

	OrderDto addOrder(@Valid OrderRequest orderRequest);

	List<OrderDto> findAllByUserId(long userId);

}
