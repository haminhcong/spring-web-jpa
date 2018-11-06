package com.spring.service.service;

import com.spring.service.entity.Customer;
import com.spring.service.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.util.concurrent.TimeUnit;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository =  customerRepository;
    }
    @HystrixCommand(fallbackMethod = "findCustomerTimeout", commandKey = "findCustomerByAddress",
            threadPoolKey = "findCustomerByAddressPool" )
    public Customer findCustomerByAddress(String address) throws InterruptedException {
//        TimeUnit.MILLISECONDS.sleep(5000);
        return customerRepository.findByAddress(address);
    }
    public Customer findCustomerByAddressFull(String address) throws InterruptedException {
        return customerRepository.findByAddress(address);
    }
    public Customer findCustomerTimeout(String address) {
        return new Customer();
    }
}
