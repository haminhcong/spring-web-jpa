package com.spring.ws.exceptions;


public class CustomerNotFoundException extends Exception {

  public CustomerNotFoundException(Long customerID) {
    super("Customer " + customerID.toString() + " not found!");
  }
}
