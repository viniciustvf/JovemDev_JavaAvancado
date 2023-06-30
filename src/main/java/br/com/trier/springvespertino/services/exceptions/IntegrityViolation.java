package br.com.trier.springvespertino.services.exceptions;

public class IntegrityViolation extends RuntimeException {

	public IntegrityViolation(String message) {
		super(message);
	}

}
