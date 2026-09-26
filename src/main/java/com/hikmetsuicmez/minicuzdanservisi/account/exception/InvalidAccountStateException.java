package com.hikmetsuicmez.minicuzdanservisi.account.exception;

public class InvalidAccountStateException extends RuntimeException{
	
	private static final long serialVersionUID = 2222587582872891370L;

	public InvalidAccountStateException(String message) {
        super(message);
    }
}
