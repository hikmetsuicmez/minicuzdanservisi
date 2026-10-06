package com.hikmetsuicmez.minicuzdanservisi.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hikmetsuicmez.minicuzdanservisi.account.entity.Account;
import com.hikmetsuicmez.minicuzdanservisi.account.entity.AccountType;

import jakarta.persistence.LockModeType;

public interface AccountRepository extends JpaRepository<Account, Long>{
	
	// Sistem hesabını güvenli bir şekilde getirmek için:
    Optional<Account> findByAccountType(AccountType type);
    
    default Account getSystemAccount() {
    	return findByAccountType(AccountType.SYSTEM)
                .orElseThrow(() -> new IllegalStateException("Sistem hesabı veritabanında bulunamadı! Migration çalışmamış olabilir."));
    }
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findByIdForUpdate(@Param("id") Long id);
}
