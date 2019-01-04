package com.spring.ws.exceptions;

public class CustomerServiceDatabaseErrorException extends RuntimeException {
  public CustomerServiceDatabaseErrorException(String msg){
    super(msg);
  }
}