package com.hikmetsuicmez.minicuzdanservisi.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikmetsuicmez.minicuzdanservisi.account.entity.Account;
import com.hikmetsuicmez.minicuzdanservisi.account.entity.AccountType;

public interface AccountRepository extends JpaRepository<Account, Long>{
	
	// Sistem hesabını güvenli bir şekilde getirmek için:
    Optional<Account> findByAccountType(AccountType type);
    
    default Account getSystemAccount() {
    	return findByAccountType(AccountType.SYSTEM)
                .orElseThrow(() -> new IllegalStateException("Sistem hesabı veritabanında bulunamadı! Migration çalışmamış olabilir."));
    }
}
