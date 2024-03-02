package com.equo.application.client.exceptions;

/**
 * The {@code AppNameNotSpecifiedException} class represents an exception that is thrown
 * when an application name is not specified.
 */
public class AppNameNotSpecifiedException extends RuntimeException {

  public AppNameNotSpecifiedException() {
    super("App name is not specified");
  }
}
