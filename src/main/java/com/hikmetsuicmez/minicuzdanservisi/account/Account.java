package com.hikmetsuicmez.minicuzdanservisi.account;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "accounts")
@Getter
public class Account {
	
	private static final String CURRENCY_TRY = "TRY";
	
	public Account(String ownerName) {
		this.ownerName = ownerName;
		this.currency = CURRENCY_TRY;
		this.status = AccountStatus.ACTIVE;
	}
	
	protected Account() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "owner_name", nullable = false)
	private String ownerName;
	
	@Column(name = "currency", nullable = false)
	private String currency;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	@CreationTimestamp
	private Instant createdAt;

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	// Herkes herhangi bir durumu herhangi bir duruma çevirebilmeli mi?
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	
}
