package com.hikmetsuicmez.minicuzdanservisi.ledger.dto;

import java.math.BigDecimal;

public record DepositResponse(
		
		Long transactionId,
		BigDecimal amount,
		BigDecimal balance
) {}
