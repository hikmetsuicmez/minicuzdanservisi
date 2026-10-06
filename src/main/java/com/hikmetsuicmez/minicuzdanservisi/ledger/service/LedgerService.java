package com.hikmetsuicmez.minicuzdanservisi.ledger.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

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
import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.TransferRequest;
import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.TransferResponse;
import com.hikmetsuicmez.minicuzdanservisi.ledger.entity.LedgerEntry;
import com.hikmetsuicmez.minicuzdanservisi.ledger.exception.InsufficientBalanceException;
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
		
		this.validateAccountForTransaction(customerAccount);

		Account systemAccount = accountRepository.getSystemAccount();
		
		String customerDescription = "Hesaba Para Yükleme - Müşteri #" + customerAccount.getId();
		
		Transaction transaction = new Transaction(
				TransactionType.DEPOSIT, 
				customerDescription,
				null
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
	
	@Transactional
	public TransferResponse transfer(TransferRequest request) {
		
		Account senderAccount = accountRepository.findByIdForUpdate(request.senderAccountId())
				.orElseThrow(() -> new AccountNotFoundException(request.senderAccountId()));
		
		BigDecimal amount = request.amount().setScale(2, RoundingMode.UNNECESSARY);

		if (Objects.equals(request.senderAccountId(), request.recipientAccountId())) {
		    throw new InvalidAccountStateException("Gönderen ve alıcı hesap aynı olamaz. Kendinize transfer yapamazsınız.");
		}
		
		
		Account recipientAccount = accountRepository.findById(request.recipientAccountId())
				.orElseThrow(() -> new AccountNotFoundException(request.recipientAccountId()));
		
		this.validateAccountForTransaction(senderAccount, recipientAccount);
		
		BigDecimal senderBalance = ledgerEntryRepository.calculateBalanceByAccountId(senderAccount.getId());
		
		if(senderBalance.compareTo(amount) < 0) {
			throw new InsufficientBalanceException(amount, senderBalance);
		}
		
		String transferDescription = "Hesaplar arası transfer";
		
		Transaction transaction = new Transaction(
				TransactionType.TRANSFER, 
				request.description().isBlank() ? transferDescription : request.description(),
				null
		);
		transactionRepository.save(transaction);	

		LedgerEntry senderLedgerEntry = new LedgerEntry(
				senderAccount, 
				transaction, 
				amount.negate()
		);
		
		LedgerEntry recipientLedgerEntry = new LedgerEntry(
				recipientAccount, 
				transaction, 
				amount
		);
		
		ledgerEntryRepository.saveAll(List.of(senderLedgerEntry, recipientLedgerEntry));

		senderBalance = ledgerEntryRepository.calculateBalanceByAccountId(senderAccount.getId());
		
		TransferResponse response = new TransferResponse(
				transaction.getId(), 
				recipientAccount.getId(), 
				amount, 
				senderBalance
		);
				
		return response;
	}
	
	private void validateAccountForTransaction(Account... accounts) {
	    for (Account account : accounts) {
	        if (account.getAccountType() != AccountType.CUSTOMER) {
	            throw new InvalidAccountStateException("Sistem hesapları üzerinde doğrudan işlem yapılamaz.");
	        }

	        if (account.getStatus() != AccountStatus.ACTIVE) {
	            throw new InvalidAccountStateException("Sadece aktif durumdaki hesaplar üzerinden işlem yapılabilir.");
	        }
	    }
	}
}
