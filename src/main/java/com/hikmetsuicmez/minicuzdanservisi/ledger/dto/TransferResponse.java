package com.hikmetsuicmez.minicuzdanservisi.ledger.dto;

import java.math.BigDecimal;

public record TransferResponse(
		
		Long transactionId,
		Long recipientAccountId,
		BigDecimal amount,
		BigDecimal senderBalance
) {}
