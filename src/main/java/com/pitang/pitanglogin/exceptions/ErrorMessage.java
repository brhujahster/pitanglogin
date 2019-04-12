package com.pitang.pitanglogin.exceptions;

public class ErrorMessage {

	private String message;
	private Integer errorCode;
	
	
	
	public ErrorMessage(String message, Integer errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	
}
