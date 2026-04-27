package com.devdad.Forma.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<?> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
		return ResponseEntity.status(403).body(ex.getMessage());
	}

	// Catch all fallback @ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneralExceptions(Exception ex) {
		return ResponseEntity.status(500).body("Message: " + ex.getMessage() + " /nCause: " + ex.getCause());
	}
}
