package com.teo.usecase3.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;

@ControllerAdvice
public class ControllerExceptions {
	private static final String RESPONSE_KEY = "message";
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationEx(MethodArgumentNotValidException ex) {
		Map<String, String> responseBody = new HashMap<>();			
		ex.getBindingResult().getAllErrors().forEach(err -> {
			String field = ((FieldError) err).getField();
			String message = err.getDefaultMessage();
			responseBody.put(field,  message);
		});
		
		return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, String>> handleDataViolationEx(DataIntegrityViolationException ex) {
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put(RESPONSE_KEY, ex.getMostSpecificCause().getMessage());
		
		return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, MissingServletRequestParameterException.class})
	public ResponseEntity<Map<String, String>> handleIllegalArgumentEx(Exception ex) {
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put(RESPONSE_KEY, ex.getMessage());
		
		return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(FeignException.class)
	public ResponseEntity<Map<String, String>> handleFeignEx(FeignException ex) {		
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
		
		HashMap<String, String> responseBody;
		try {
			responseBody = mapper.readValue(ex.contentUTF8(), typeRef);
		} catch (Exception e) {
			responseBody = new HashMap<>();
			responseBody.put(RESPONSE_KEY, ex.getMessage());			
		}
		
		return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	}
}
