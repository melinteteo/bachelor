package com.teo.usecase3.feignclient;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teo.usecase3.dto.bankclient.TransactionDto;
import com.teo.usecase3.dto.bankclient.TransferRequestDto;
import com.teo.usecase3.utils.AppConstants;

@FeignClient(name = AppConstants.BANK_SERVICE_URL + AppConstants.TRANSACTION_CONTROLLER)
public interface BankClient {
	
	@PostMapping(AppConstants.TRANSFER_FUND)
	public TransactionDto transferFund(@Valid @RequestBody TransferRequestDto transferDto);
}
