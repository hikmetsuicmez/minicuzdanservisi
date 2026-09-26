package com.hikmetsuicmez.minicuzdanservisi.transaction.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "transactions")
@Getter
@Immutable
public class Transaction {
	
	protected Transaction() {}
	
	public Transaction(TransactionType type, String description) {
		this.type = type;
		this.description = description;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "transaction_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	
	private String description;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	@CreationTimestamp
	private Instant createdAt;

}
