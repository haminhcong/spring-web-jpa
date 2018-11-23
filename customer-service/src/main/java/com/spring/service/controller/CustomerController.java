package com.spring.service.controller;

import com.spring.service.entity.Customer;
import com.spring.service.exception.AppException;
import com.spring.service.repository.CustomerRepository;
import com.spring.service.service.AddrProxy1Service;
import com.spring.service.service.AddrSingletonExceptionService;
import com.spring.service.service.CustomerService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api")
public class CustomerController {

  private CustomerRepository customerRepository;
  private Long counter;
    private CustomerService customerService;
//  private ObjectProvider<CustomerService> customerServiceFactory;
  private AddrProxy1Service addrProxy1Service;
  private AddrSingletonExceptionService addrSingletonExceptionService;

  @Autowired
  public CustomerController(
      CustomerRepository customerRepository,
//      ObjectProvider<CustomerService> customerServiceFactory,
      AddrProxy1Service addrProxy1Service,
      CustomerService customerService,
      AddrSingletonExceptionService addrSingletonExceptionService) {
    this.customerRepository = customerRepository;
//    this.customerServiceFactory = customerServiceFactory;
    this.addrProxy1Service = addrProxy1Service;
    this.addrSingletonExceptionService = addrSingletonExceptionService;
    this.customerService=customerService;
    this.counter = 0L;
  }

  // Get All Customers
  @GetMapping("/customers")
  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

//  @GetMapping(value = "/customers", params = "address")
//  public Customer getCustomersWithAddress(@RequestParam("address") String address)
//      throws InterruptedException {
//    TimeUnit.MILLISECONDS.sleep(1000);
//    this.counter += 1;
//    Customer customer = new Customer();
//    CustomerService customerService = customerServiceFactory.getObject(3);
//    customer.setId((long) customerService.getCustomerServiceCounter());
//    return customer;
//        return customerRepository.findByAddress(address);
//  }

  @GetMapping(value = "/counter", params = "type")
  public List<Integer> getCounter(@RequestParam("type") int type) {
    addrProxy1Service.setAddrSingleton(type);
    AddrProxy1Service addrProxy1Service2 = addrProxy1Service;
    return addrProxy1Service2.getCounter();
  }

  @GetMapping(value = "/check-exception", params = "type")
  public String checkException(@RequestParam("type") int type) {
    try {
      return String.valueOf(addrSingletonExceptionService.getCounter(type));
    } catch (AppException appEx) {
      return "check-exception - app exception - " + appEx.getMessage();
    } catch (Exception ex) {
      return "check-exception - generic exception - " + ex.getMessage();
    }
  }

  @GetMapping(value = "/add-customer", params = {"username", "address"})
  public String addCustomer(
      @RequestParam("username") String username,
      @RequestParam("address") String address) {
    Customer newCustomer = new Customer();
    newCustomer.setUserName(username);
    newCustomer.setAddress(address);
    try {
      customerService.addNewCustomer(newCustomer);
    } catch (AppException e) {
      e.printStackTrace();
      return "error";
    }
    return "done";
  }


}