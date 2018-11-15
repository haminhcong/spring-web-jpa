package com.spring.service.controller;

import com.spring.service.entity.Customer;
import com.spring.service.exception.AppException;
import com.spring.service.repository.CustomerRepository;
import com.spring.service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
//@RequestMapping("/api")
public class CustomerController {
    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerRepository customerRepository,
                              CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    // Get All Customers
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping(value = "/customers", params = "address")
    public Customer getCustomersWithAddress(@RequestParam("address") String address) throws InterruptedException {
        return customerService.findCustomerByAddress(address);
    }

    @GetMapping(value = "/customers-full", params = "address")
    public Customer getCustomerWithFullAddr(@RequestParam("address") String address) throws InterruptedException {
        return customerService.findCustomerByAddressFull(address);

    }

    @GetMapping(value = "/update-customer", params = {"id", "address"})
    public Customer updateCustomer(@RequestParam("id") Long id, @RequestParam("address") String address) {
        try {
            return customerService.updateCustomer(id, address);
        } catch (AppException appEx) {
            System.out.print(appEx.getMessage());
            return null;
        }
    }

    @GetMapping(value = "/customer", params = "id")
    public Customer getCustomer(@RequestParam("id") Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}