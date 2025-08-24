package com.babytrackmaster.api_gastos.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) { super(msg); }

	public NotFoundException() {
		super();
	}
    
    
}