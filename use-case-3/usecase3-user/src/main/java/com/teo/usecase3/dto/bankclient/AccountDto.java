package com.teo.usecase3.dto.bankclient;

import java.util.Map;

public class AccountDto {
	
	private Map<String, String> client;
	private String accountNumber;
	
	public Map<String, String> getClient() {
		return client;
	}
	public void setClient(Map<String, String> client) {
		this.client = client;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
