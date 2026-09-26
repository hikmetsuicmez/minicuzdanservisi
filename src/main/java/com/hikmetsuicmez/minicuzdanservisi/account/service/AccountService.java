package com.hikmetsuicmez.minicuzdanservisi.account.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.hikmetsuicmez.minicuzdanservisi.account.dto.AccountResponse;
import com.hikmetsuicmez.minicuzdanservisi.account.dto.CreateAccountRequest;
import com.hikmetsuicmez.minicuzdanservisi.account.entity.Account;
import com.hikmetsuicmez.minicuzdanservisi.account.exception.AccountNotFoundException;
import com.hikmetsuicmez.minicuzdanservisi.account.repository.AccountRepository;
import com.hikmetsuicmez.minicuzdanservisi.ledger.repository.LedgerEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	private final LedgerEntryRepository ledgerEntryRepository;
	
	public AccountResponse createAccount(CreateAccountRequest request) {
		
		Account account = new Account(request.ownerName());
		Account savedAccount = accountRepository.save(account);
		AccountResponse response = AccountResponse.fromEntity(savedAccount, BigDecimal.ZERO);
		
		return response;
	}
	
	public AccountResponse retrieveAccount(Long accountId) {
		
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountNotFoundException(accountId));
		
		BigDecimal balance = ledgerEntryRepository.calculateBalanceByAccountId(accountId);
		
		AccountResponse response = AccountResponse.fromEntity(account, balance);
		
		return response;
	}
}
