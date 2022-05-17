package com.teo.usecase3.service;

import java.util.List;

public interface CrudService<T, I> {
	
	List<T> findAll();
	
	T findById(I id);
	
	T save(T object);
	
	void deleteById(I id);
}
