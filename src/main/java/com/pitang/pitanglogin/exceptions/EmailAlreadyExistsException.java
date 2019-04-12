package com.pitang.pitanglogin.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -5894385551578554040L;
	
	public EmailAlreadyExistsException(String msg) {
		super(msg);
	}
}
