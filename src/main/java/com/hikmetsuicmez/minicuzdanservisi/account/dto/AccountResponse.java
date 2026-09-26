package com.hikmetsuicmez.minicuzdanservisi.account.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.hikmetsuicmez.minicuzdanservisi.account.entity.Account;
import com.hikmetsuicmez.minicuzdanservisi.account.entity.AccountStatus;

public record AccountResponse(

		Long id,
		String ownerName,
		String currency,
		BigDecimal balance,
		AccountStatus status,
		Instant createdAt
		
) {
	public static AccountResponse fromEntity(Account account, BigDecimal balance) {
		return new AccountResponse(
				account.getId(),
				account.getOwnerName(), 
				account.getCurrency(),
				balance,
				account.getStatus(), 
				account.getCreatedAt()
		);
	}
}
