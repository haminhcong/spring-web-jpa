package com.spring.service.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Customer")  // 404
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerID) {
        super("Customer" + customerID.toString() + " not found");
    }
}
