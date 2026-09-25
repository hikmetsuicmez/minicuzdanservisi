package com.hikmetsuicmez.minicuzdanservisi.account.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hikmetsuicmez.minicuzdanservisi.account.dto.AccountResponse;
import com.hikmetsuicmez.minicuzdanservisi.account.dto.CreateAccountRequest;
import com.hikmetsuicmez.minicuzdanservisi.account.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/accounts")
public class AccountController {

	private final AccountService accountService;
	
	@PostMapping
	public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest) {
		
		AccountResponse response = accountService.createAccount(accountRequest);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(response.id())
				.toUri();
		
		return ResponseEntity.created(location).body(response);
	}
	
	@GetMapping("/{accountId}")
	public ResponseEntity<AccountResponse> retrieveAccount(@PathVariable Long accountId) {
		AccountResponse response = accountService.retrieveAccount(accountId);
		return ResponseEntity.ok(response);
	}	
}
