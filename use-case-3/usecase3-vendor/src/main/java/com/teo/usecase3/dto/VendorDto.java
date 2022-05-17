package com.teo.usecase3.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class VendorDto {
	
	private long id;
	
	private String name;
	
	private String description;
	
	@JsonIgnoreProperties({"vendor"})
	private Set<ProductDto> products = new HashSet<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Set<ProductDto> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductDto> products) {
		this.products = products;
	}
	
}
