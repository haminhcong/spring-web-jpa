package com.spring.service.service;

import com.spring.service.dto.CustomerDTO;
import com.spring.service.dto.TotalOrdersDTO;
import com.spring.service.entity.Customer;
import com.spring.service.http_client.OrderV1Client;
import com.spring.service.repository.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private CustomerRepository customerRepository;
  private OrderV1Client orderV1Client;

  @Autowired
  public CustomerService(
      CustomerRepository customerRepository,
      OrderV1Client orderV1Client) {
    this.customerRepository = customerRepository;
    this.orderV1Client = orderV1Client;
  }

  public List<Customer> getCustomerList() {
    return customerRepository.findAll();
  }

  public Customer getCustomer(Long id) {
    return customerRepository.findById(id).orElse(null);
  }

  public CustomerDTO getCustomerDetail(Customer customer) {
    TotalOrdersDTO totalCustomerOrders = orderV1Client
        .getCustomerOrders(customer.getId());
    return getCustomerDetail(customer, totalCustomerOrders.getTotalOrders());
  }


  private CustomerDTO getCustomerDetail(Customer customer, int totalOrders) {
    CustomerDTO customerDetail = new CustomerDTO();
    customerDetail.setUserName(customer.getUserName());
    customerDetail.setAddress(customer.getAddress());
    customerDetail.setGender(customer.getGender());
    customerDetail.setPhoneNumber(customer.getPhoneNumber());
    customerDetail.setTotalOrders(totalOrders);
    return customerDetail;
  }
}
