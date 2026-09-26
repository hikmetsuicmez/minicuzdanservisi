package com.hikmetsuicmez.minicuzdanservisi.ledger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.DepositRequest;
import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.DepositResponse;
import com.hikmetsuicmez.minicuzdanservisi.ledger.service.LedgerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/accounts/{accountId}/deposits")
@RequiredArgsConstructor
public class DepositController {

	private final LedgerService ledgerService;
	
	@PostMapping
	public ResponseEntity<DepositResponse> deposit(@PathVariable Long accountId,@RequestBody @Valid DepositRequest request) {
		DepositResponse response = ledgerService.deposit(accountId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
