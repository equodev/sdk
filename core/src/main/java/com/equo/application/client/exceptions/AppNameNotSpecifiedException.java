package com.equo.application.client.exceptions;

public class AppNameNotSpecifiedException extends RuntimeException {

    public AppNameNotSpecifiedException() {
        super("App name is not specified");
    }
}
