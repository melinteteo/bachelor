package com.teo.usecase3.dto.bankclient;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TransactionDto {
	
	private long id;
	
	private AccountDto fromAccount;
	
	private AccountDto toAccount;
	
	private int value;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateTime;
	
	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AccountDto getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(AccountDto fromAccount) {
		this.fromAccount = fromAccount;
	}

	public AccountDto getToAccount() {
		return toAccount;
	}

	public void setToAccount(AccountDto toAccount) {
		this.toAccount = toAccount;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
