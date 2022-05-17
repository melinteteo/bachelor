package com.teo.usecase3.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptions {
	
	@ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, MissingServletRequestParameterException.class})
	public ResponseEntity<Map<String, String>> handleIllegalArgumentEx(Exception ex) {
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("message", ex.getMessage());
		
		return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	}
	
}
