package com.teo.usecase3.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Utils {
	
	private Utils() {
	    throw new IllegalStateException("Utility class");
	  }

	
	public static void checkResourcePath(String path) {
		Resource resource = new ClassPathResource(path);
		if (!resource.exists()) {
			throw new IllegalArgumentException("Wrong path for the resource!");
		}
	}
	
}
