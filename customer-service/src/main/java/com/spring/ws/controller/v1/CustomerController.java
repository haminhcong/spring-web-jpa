package com.spring.ws.controller.v1;

import com.spring.ws.dto.CustomerDTO;
import com.spring.ws.entity.Customer;
import com.spring.ws.exceptions.CustomerNotFoundException;
import com.spring.ws.service.CustomerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
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
  @GetMapping(value = "/customers", params = "id")
  public CustomerDTO getCustomer(@RequestParam("id") Long id) {
    Customer customer = customerService.getCustomer(id);
    if (customer == null) {
      throw new CustomerNotFoundException(id);
    }
    return customerService.getCustomerDetail(customer);

  }

}