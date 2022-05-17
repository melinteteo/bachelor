package com.teo.usecase3.dto;

import javax.validation.constraints.Min;

public class OrderItemRequest {
	
	@Min(value=0, message="Product id must be positive!")
	private long productId;
	
	@Min(value=1, message="The number of products must be bigger than or equal to 1!")
	private int numberOfPieces;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getNumberOfPieces() {
		return numberOfPieces;
	}

	public void setNumberOfPieces(int numberOfPieces) {
		this.numberOfPieces = numberOfPieces;
	}

}
