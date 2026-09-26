package com.hikmetsuicmez.minicuzdanservisi.ledger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.TransferRequest;
import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.TransferResponse;
import com.hikmetsuicmez.minicuzdanservisi.ledger.service.LedgerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/transfers")
public class TransferController {

	private final LedgerService ledgerService;
	
	@PostMapping
	public ResponseEntity<TransferResponse> transfer(@RequestBody @Valid TransferRequest request) {
		TransferResponse response = ledgerService.transfer(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
