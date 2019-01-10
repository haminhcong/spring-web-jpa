package com.spring.ws.exceptions;

public class ExternalServiceErrorException extends Exception {

  public ExternalServiceErrorException(String message) {
    super(message);
  }
}
