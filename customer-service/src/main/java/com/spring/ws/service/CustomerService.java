package com.spring.ws.service;

import com.spring.ws.dto.CustomerDTO;
import com.spring.ws.dto.TotalOrdersDTO;
import com.spring.ws.entity.Customer;
import com.spring.ws.http_client.OrderV1Client;
import com.spring.ws.repository.CustomerRepository;
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
