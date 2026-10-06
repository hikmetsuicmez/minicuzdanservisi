package com.hikmetsuicmez.minicuzdanservisi.ledger;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.hikmetsuicmez.minicuzdanservisi.TestcontainersConfiguration;
import com.hikmetsuicmez.minicuzdanservisi.account.dto.AccountResponse;
import com.hikmetsuicmez.minicuzdanservisi.account.dto.CreateAccountRequest;
import com.hikmetsuicmez.minicuzdanservisi.account.entity.Account;
import com.hikmetsuicmez.minicuzdanservisi.account.repository.AccountRepository;
import com.hikmetsuicmez.minicuzdanservisi.account.service.AccountService;
import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.DepositRequest;
import com.hikmetsuicmez.minicuzdanservisi.ledger.dto.TransferRequest;
import com.hikmetsuicmez.minicuzdanservisi.ledger.exception.InsufficientBalanceException;
import com.hikmetsuicmez.minicuzdanservisi.ledger.repository.LedgerEntryRepository;
import com.hikmetsuicmez.minicuzdanservisi.ledger.service.LedgerService;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class LedgerServiceTest {

	@Autowired
	private LedgerService ledgerService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private LedgerEntryRepository entryRepository;
	
	@Test
	@DisplayName("Başarılı bir para yükleme işleminde hesap bakiyesi güncellenmelidir")
	void shouldIncreaseBalanceWhenDepositIsSuccessful() {
		
		CreateAccountRequest accountRequest = new CreateAccountRequest("Bakiye Test");
		AccountResponse accountResponse = accountService.createAccount(accountRequest);
		
		BigDecimal depositAmount = new BigDecimal("100.00");
		
		DepositRequest depositRequest = new DepositRequest(depositAmount);
		ledgerService.deposit(accountResponse.id(), depositRequest);
		
		Account updatedAccount = accountRepository.findById(accountResponse.id())
                .orElseThrow(() -> new AssertionError("Hesap veritabanında bulunamadı!"));
	
		BigDecimal balance = entryRepository.calculateBalanceByAccountId(updatedAccount.getId());
		assertThat(balance).isEqualByComparingTo(new BigDecimal("100.00")); // veya "100"
	}
	
	@Test
	@DisplayName("Eşzamanlı 50 transfer isteğinde bakiye eksiye düşmemeli ve tam 10 işlem başarılı olmalıdır")
	void shouldMaintainBalanceIntegrityUnderConcurrentTransfers() throws InterruptedException {
		CreateAccountRequest senderAccountRequest = new CreateAccountRequest("Sender");
		CreateAccountRequest recipientAccountRequest = new CreateAccountRequest("Recipient");
		
		AccountResponse senderAccountResponse = accountService.createAccount(senderAccountRequest);
		AccountResponse recipientAccountResponse = accountService.createAccount(recipientAccountRequest);
		
		BigDecimal senderDepositAmount = new BigDecimal("100.00");
		DepositRequest senderDepositRequest = new DepositRequest(senderDepositAmount);
		ledgerService.deposit(senderAccountResponse.id(), senderDepositRequest);
		
		int threadCount = 50;
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(1);

		AtomicInteger successCount = new AtomicInteger(0);
	    AtomicInteger failureCount = new AtomicInteger(0);
	    
	    TransferRequest request = new TransferRequest(senderAccountResponse.id(), recipientAccountResponse.id(), new BigDecimal("10.00"), "eşzamanlı test");
	    
	    Queue<Exception> unexpectedErrors = new ConcurrentLinkedQueue<>();

	    for (int i = 0; i < threadCount; i++) {
	    	executor.submit(() -> {
	    		try {
	    			latch.await();
	    			ledgerService.transfer(request);
	    			successCount.incrementAndGet();
	    		} catch (InsufficientBalanceException e) {
	    			failureCount.incrementAndGet();
	    		} catch (Exception e) {
	    			unexpectedErrors.add(e);
	    		}
	    	});
	    }
	    
	    latch.countDown();
	    executor.shutdown();
	    boolean finished =  executor.awaitTermination(10, TimeUnit.SECONDS);
	    
		BigDecimal senderBalance = entryRepository.calculateBalanceByAccountId(senderAccountResponse.id());
		BigDecimal recipientBalance = entryRepository.calculateBalanceByAccountId(recipientAccountResponse.id());

		System.out.println(successCount.get());
		System.out.println(failureCount.get());
		
		assertThat(finished).isTrue();
		assertThat(unexpectedErrors).isEmpty();
	    assertThat(successCount.get()).isEqualTo(10);
	    assertThat(failureCount.get()).isEqualTo(40);
	    assertThat(senderBalance).isEqualByComparingTo(BigDecimal.ZERO);
	    assertThat(recipientBalance).isEqualByComparingTo(new BigDecimal("100.00"));
	    		
	}
}
