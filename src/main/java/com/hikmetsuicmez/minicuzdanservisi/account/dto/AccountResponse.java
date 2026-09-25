package com.hikmetsuicmez.minicuzdanservisi.account.dto;

import java.time.Instant;

import com.hikmetsuicmez.minicuzdanservisi.account.entity.Account;
import com.hikmetsuicmez.minicuzdanservisi.account.entity.AccountStatus;

public record AccountResponse(

		Long id,
		String ownerName,
		String currency,
		AccountStatus status,
		Instant createdAt
		
) {
	public static AccountResponse fromEntity(Account account) {
		return new AccountResponse(
				account.getId(),
				account.getOwnerName(), 
				account.getCurrency(), 
				account.getStatus(), 
				account.getCreatedAt()
		);
	}
}
