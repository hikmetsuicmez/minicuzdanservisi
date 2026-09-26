package com.hikmetsuicmez.minicuzdanservisi.ledger.entity;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import com.hikmetsuicmez.minicuzdanservisi.account.entity.Account;
import com.hikmetsuicmez.minicuzdanservisi.transaction.entity.Transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "ledger_entries")
@Getter
@Immutable
public class LedgerEntry {
	
	protected LedgerEntry() {}
	
	public LedgerEntry(Account account, Transaction transaction, BigDecimal amount) {
		this.account = account;
		this.transaction = transaction;
		this.amount = amount;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	@CreationTimestamp
	private Instant createdAt;
}
