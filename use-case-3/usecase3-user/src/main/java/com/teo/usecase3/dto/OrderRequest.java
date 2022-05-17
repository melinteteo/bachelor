package com.teo.usecase3.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class OrderRequest {
	
	@Min(value=1, message="User id must be greater than or equal to 1!")
	private long userId;
	
	@NotEmpty (message="Account number cannot be null/empty!")
	@Size(min=6, max=6, message="Account number must have 6 digits!")
	@Pattern(regexp="^\\d{6}$", message="Please specify a correct sender account!")
	private String fromAccountNumber;
	
	@Valid
	@NotNull(message="The items must be specified!")
	@Size(min=1, message="At least one item must be bought!")
	private List<OrderItemRequest> items;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public List<OrderItemRequest> getItems() {
		return items;
	}

	public void setItems(List<OrderItemRequest> items) {
		this.items = items;
	}
	
}
