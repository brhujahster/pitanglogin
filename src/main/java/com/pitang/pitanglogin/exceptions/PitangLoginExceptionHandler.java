package com.pitang.pitanglogin.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class PitangLoginExceptionHandler extends ResponseEntityExceptionHandler {

	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler({EmailAlreadyExistsException.class})
	public ResponseEntity<Object> handleEmailAlreadyExistException(EmailAlreadyExistsException ex, WebRequest request) {
		String mensagem = messageSource.getMessage("exceptions.email-already-exists", 
				null, LocaleContextHolder.getLocale());
		
		ErrorMessage errorMessage = new ErrorMessage(mensagem, 100);
		return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({InvalidFieldsException.class})
	public ResponseEntity<Object> handleInvalidFieldsException(InvalidFieldsException ex, WebRequest request) {
		String mensagem = messageSource.getMessage("exceptions.invalid-fields", 
				null, LocaleContextHolder.getLocale());
		
		ErrorMessage errorMessage = new ErrorMessage(mensagem, 101);
		return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}
