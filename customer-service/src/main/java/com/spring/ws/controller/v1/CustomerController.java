package com.spring.ws.controller.v1;

import com.netflix.client.ClientException;
import com.spring.ws.dto.response.CustomerDTO;
import com.spring.ws.dto.response.CustomerListResponseDTO;
import com.spring.ws.dto.response.CustomerLocationInfoDTO;
import com.spring.ws.entity.Customer;
import com.spring.ws.exceptions.CustomerNotFoundException;
import com.spring.ws.exceptions.CustomerServiceDatabaseErrorException;
import com.spring.ws.exceptions.ExternalServiceErrorException;
import com.spring.ws.service.CustomerService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1")
public class CustomerController {

  private CustomerService customerService;

  @Autowired
  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping("/customers")
  // TODO: Paginator ?
  public CustomerListResponseDTO getAllCustomers(HttpServletRequest req) {
    try {
      List<Customer> customerList;
      customerList = customerService.getCustomerList();
      return new CustomerListResponseDTO(customerList);
    } catch (Exception ex) {
      String errorMessage = "Fail to get customer list from database";
      log.error(errorMessage, ex);
      throw new CustomerServiceDatabaseErrorException(errorMessage);
    }
  }

  // TODO: Implements API for customer role, this API is for admin role
  @GetMapping(value = "/customers", params = "id")
  public CustomerDTO getCustomer(@RequestParam("id") Long id)
      throws CustomerNotFoundException, ExternalServiceErrorException {
    CustomerDTO customer;
    try {
      customer = customerService.getCustomerDetail(id);
    }catch (ExternalServiceErrorException ex) {
      String errorMessage = "Fail to get customer " + id + ". Reason: " + ex.getMessage();
      log.error(errorMessage, ex);
      throw new ExternalServiceErrorException(errorMessage);
    }
    catch (Exception ex) {
      String errorMessage = "Fail to get customer " + id + ". Reason: " + ex.getMessage();
      log.error(errorMessage, ex);
      throw new CustomerServiceDatabaseErrorException(errorMessage);
    }
    if (customer == null) {
      throw new CustomerNotFoundException(id);
    }
    return customer;
  }

  @GetMapping(value = "/customer-geo-info")
  public CustomerLocationInfoDTO getCustomerGeoInfo(HttpServletRequest req)
      throws ExternalServiceErrorException {

    String customerIPAddress = req.getHeader("X-FORWARDED-FOR");
    if (customerIPAddress == null) {
      customerIPAddress = req.getRemoteAddr();
    }

    try {
      return customerService.getCustomerLocationInfoFromIP(customerIPAddress);
    } catch (Exception e) {
      String errorMessage = "Fail to get IP " + customerIPAddress + " Info from IP Stack Server";
      log.error(errorMessage, e);
      throw new ExternalServiceErrorException(errorMessage);
    }
  }

}