package com.spring.service.controller;

import com.spring.service.entity.Customer;
import com.spring.service.repository.CustomerRepository;
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

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Get All Customers
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping(value = "/customers", params = "address")
    public Customer getCustomersWithAddress(@RequestParam("address") String address) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
        return new Customer();
//        return customerRepository.findByAddress(address);
    }

}