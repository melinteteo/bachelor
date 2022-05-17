package com.teo.usecase3.utils;

public class AppConstants {
	public static final String GET_PRODUCTS = "/products";
	public static final String ADD_ORDER = "/orders";
	public static final String GET_USER_ORDERS = "/{userId}/orders";
	
	/*
	 * Feign Client
	 */
	public static final String VENDOR_SERVICE_URL = "http://VENDOR-SERVICE/vendor";
	public static final String VENDOR_PRODUCT_CONTROLLER = "/products";
	public static final String VENDOR_SEARCH_PRODUCT = "/search";
	public static final String VENDOR_LIST_PRODUCT = "/list";
	public static final String BANK_SERVICE_URL = "http://BANK-SERVICE/bank";
	public static final String TRANSACTION_CONTROLLER = "/transfer";
	public static final String TRANSFER_FUND = "/fundTransfer";
	
	private AppConstants() {
	    throw new IllegalStateException("Constants class");
	  }
}
