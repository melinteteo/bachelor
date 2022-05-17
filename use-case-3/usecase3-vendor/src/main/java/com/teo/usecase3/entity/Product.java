package com.teo.usecase3.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Product {
	@Id
	private long id;
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	@JsonIgnoreProperties({"products"})
	private Vendor vendor;
	
	private String name;
	
	private String description;
	
	private int price;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
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
