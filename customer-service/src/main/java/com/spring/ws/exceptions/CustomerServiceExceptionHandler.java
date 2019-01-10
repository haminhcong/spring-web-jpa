package com.spring.ws.exceptions;

import com.spring.ws.dto.response.HTTPErrorResponseDTO;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class CustomerServiceExceptionHandler {


  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(CustomerNotFoundException.class)
  @ResponseBody HTTPErrorResponseDTO handleCustomerNotFoundException(
      HttpServletRequest req, CustomerNotFoundException ex) {
    log.warn(ex.getMessage());
    return new HTTPErrorResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(CustomerServiceDatabaseErrorException.class)
  @ResponseBody HTTPErrorResponseDTO handleCustomerListErrorException(
      HttpServletRequest req, CustomerServiceDatabaseErrorException ex) {
    return new HTTPErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ExceptionHandler(ExternalServiceErrorException.class)
  @ResponseBody HTTPErrorResponseDTO handleExternalServiceErrorException(
      HttpServletRequest req, ExternalServiceErrorException ex) {
    String responseMessage = "Service is currently unavailable, try again later";
    return new HTTPErrorResponseDTO(HttpStatus.SERVICE_UNAVAILABLE.value(), responseMessage);
  }
}
