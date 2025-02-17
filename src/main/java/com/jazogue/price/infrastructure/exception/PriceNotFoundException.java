package com.jazogue.price.infrastructure.exception;

public class PriceNotFoundException extends RuntimeException {
	public PriceNotFoundException(String message) {
		super(message);
	}
}
