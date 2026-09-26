package com.hikmetsuicmez.minicuzdanservisi.ledger.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hikmetsuicmez.minicuzdanservisi.account.entity.Account;
import com.hikmetsuicmez.minicuzdanservisi.account.entity.AccountStatus;
import com.hikmetsuicmez.minicuzdanservisi.account.entity.AccountType;
import com.hikmetsuicmez.minicuzdanservisi.account.exception.AccountNotFoundException;
import com.hikmetsuicmez.minicuzdanservisi.account.exception.InvalidAccountStateException;
import com.hikmetsuicmez.minicuzdanservisi.account.repository.AccountRepository;
import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.DepositRequest;
import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.DepositResponse;
import com.hikmetsuicmez.minicuzdanservisi.ledger.entity.LedgerEntry;
import com.hikmetsuicmez.minicuzdanservisi.ledger.repository.LedgerEntryRepository;
import com.hikmetsuicmez.minicuzdanservisi.transaction.entity.Transaction;
import com.hikmetsuicmez.minicuzdanservisi.transaction.entity.TransactionType;
import com.hikmetsuicmez.minicuzdanservisi.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerService {

	private final LedgerEntryRepository ledgerEntryRepository;
	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	
	@Transactional
	public DepositResponse deposit(Long accountId,DepositRequest request) {
		
		BigDecimal amount = request.amount().setScale(2, RoundingMode.UNNECESSARY);
		
		Account customerAccount = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountNotFoundException(accountId));
		
		if (customerAccount.getAccountType() != AccountType.CUSTOMER) {
		    throw new InvalidAccountStateException("Sistem hesaplarına doğrudan para yüklemesi yapılamaz.");
		}
		
		if (customerAccount.getStatus() != AccountStatus.ACTIVE) {
		    throw new InvalidAccountStateException("Sadece aktif durumdaki hesaplara para yüklemesi yapılabilir.");
		}

		Account systemAccount = accountRepository.getSystemAccount();
		
		String customerDescription = "Hesaba Para Yükleme - Müşteri #" + customerAccount.getId();
		
		Transaction transaction = new Transaction(
				TransactionType.DEPOSIT, 
				customerDescription
		);	
		transactionRepository.save(transaction);	
		
		LedgerEntry customerLedgerEntry = new LedgerEntry(
				customerAccount, 
				transaction, 
				amount
		);
		
		LedgerEntry systemLedgerEntry = new LedgerEntry(
				systemAccount, 
				transaction, 
				amount.negate()
		);
		
		ledgerEntryRepository.saveAll(List.of(customerLedgerEntry, systemLedgerEntry));
		
		BigDecimal balance = ledgerEntryRepository.calculateBalanceByAccountId(accountId);
		
		DepositResponse response = new DepositResponse(
				transaction.getId(),
				amount,
				balance
		);
		
		return response;
	}
}
