package com.teo.usecase3.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Vendor {
	@Id
	private long id;
	
	private String name;
	
	private String description;
	
	@OneToMany(mappedBy="vendor")
	private Set<Product> products = new HashSet<>();

	// relationship consistency
	public void addProduct(Product product) {
		products.add(product);
		product.setVendor(this);
	}
	
	public void removeProduct(Product product) {
		products.remove(product);
		product.setVendor(null);
	}

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

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
}
