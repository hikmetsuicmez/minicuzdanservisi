package com.hikmetsuicmez.minicuzdanservisi.ledger.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException{

	private static final long serialVersionUID = 7414145669055222602L;

	public InsufficientBalanceException(String message) {
		super(message);
	}
	
	public InsufficientBalanceException(BigDecimal amount, BigDecimal balance) {
		super("Hesap bakiyen:  " + balance + ", tranfer etmek istediğin tutardan daha az: " + amount);
	}
}
