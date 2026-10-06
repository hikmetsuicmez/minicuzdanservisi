package com.hikmetsuicmez.minicuzdanservisi.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikmetsuicmez.minicuzdanservisi.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
	
}
