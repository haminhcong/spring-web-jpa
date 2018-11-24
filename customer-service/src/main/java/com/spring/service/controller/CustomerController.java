package com.spring.service.controller;

import com.spring.service.dto.CustomerDTO;
import com.spring.service.entity.Customer;
import com.spring.service.exceptions.CustomerNotFoundException;
import com.spring.service.service.CustomerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api")
public class CustomerController {

  private CustomerService customerService;

  @Autowired
  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping("/customers")
  public List<Customer> getAllCustomers() {
    return customerService.getCustomerList();
  }

  // TODO: Replace this API with session style
  @GetMapping(value = "/customers")
  public CustomerDTO getCustomer(@RequestParam("id") Long id) {
    Customer customer = customerService.getCustomer(id);
    if (customer == null) {
      throw new CustomerNotFoundException(id);
    }
    return customerService.getCustomerDetail(customer);

  }

}