package com.equo.application.client;

public class UnsetFieldException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public UnsetFieldException(String field) {
        super("Nothing has been set for the " + field + ".");
    }
}