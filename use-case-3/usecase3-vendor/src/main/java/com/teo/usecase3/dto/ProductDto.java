package com.teo.usecase3.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ProductDto {
	
	private long id;
	
	@JsonIgnoreProperties({"id", "products"})
	private VendorDto vendor;
	
	private String name;
	
	private String description;
	
	private int price;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VendorDto getVendor() {
		return vendor;
	}

	public void setVendor(VendorDto vendor) {
		this.vendor = vendor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
}
