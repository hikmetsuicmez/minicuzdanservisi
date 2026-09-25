package com.hikmetsuicmez.minicuzdanservisi.account.exception;

public class AccountNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -387827408071288551L;

	public AccountNotFoundException(String message) {
		super(message);
	}
	
	public AccountNotFoundException(Long accountId) {
		super("Belirtilen ID ile hesap bulunamadı: " + accountId);
	}

}
