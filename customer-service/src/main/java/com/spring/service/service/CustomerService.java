package com.spring.service.service;

import com.spring.service.dto.CustomerDTO;
import com.spring.service.dto.OrderDTO;
import com.spring.service.entity.Customer;
import com.spring.service.http_client.OrderClient;
import com.spring.service.repository.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private CustomerRepository customerRepository;
  private OrderClient orderClient;

  @Autowired
  public CustomerService(
      CustomerRepository customerRepository,
      OrderClient orderClient) {
    this.customerRepository = customerRepository;
    this.orderClient = orderClient;
  }

  public List<Customer> getCustomerList() {
    return customerRepository.findAll();
  }

  public Customer getCustomer(Long id) {
    return customerRepository.findById(id).orElse(null);
  }

  public CustomerDTO getCustomerDetail(Customer customer) {
    List<OrderDTO> customerOrderList = orderClient.getCustomerOrders(customer.getId());
    return getCustomerDetail(customer, customerOrderList);
  }


  private CustomerDTO getCustomerDetail(Customer customer, List<OrderDTO> orderDTOList) {
    CustomerDTO customerDetail = new CustomerDTO();
    customerDetail.setUserName(customer.getUserName());
    customerDetail.setAddress(customer.getAddress());
    customerDetail.setGender(customer.getGender());
    customerDetail.setPhoneNumber(customer.getPhoneNumber());
    customerDetail.setOrders(orderDTOList);
    return customerDetail;
  }
}
