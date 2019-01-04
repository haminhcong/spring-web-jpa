package com.spring.ws.service;


import static org.mockito.Mockito.when;

import com.spring.ws.dto.CustomerDTO;
import com.spring.ws.dto.TotalOrdersDTO;
import com.spring.ws.entity.Customer;
import com.spring.ws.http_client.OrderV1Client;
import com.spring.ws.repository.CustomerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private OrderV1Client orderV1Client;

  private CustomerService customerService;

  private List<Customer> customerList = new ArrayList<>();
  private Customer customer1;
  private Customer customer2;

  @Before
  public void setUp() {
    customerService = new CustomerService(this.customerRepository, this.orderV1Client);
    customer1 = new Customer(1L, "ddd", "abcd", "bcd", "male", "Ha Noi", "123132132");
    customer2 = new Customer(2L, "def", "cab", "bcd", "female", "Ho Chi Minh City", "1231545243");
    customerList.add(customer1);
    customerList.add(customer2);

  }

  @Test
  public void getCustomerListNormalReturnTest() throws Exception {
    when(customerRepository.findAll()).thenReturn(customerList);
    List<Customer> customerListResponse = customerService.getCustomerList();
    Assert.assertEquals(customerList, customerListResponse);
  }

  @Test(expected = Exception.class)
  public void getCustomerListThrowExceptionTest() throws Exception {
    when(customerRepository.findAll()).thenThrow(new Exception("connection error"));
    customerService.getCustomerList();
  }


  @Test
  public void getCustomerDetailNormalReturnTest() throws Exception {
    Long customerId = 1L;
    TotalOrdersDTO totalOrders = new TotalOrdersDTO(3);
    when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer1));
    when(orderV1Client.getCustomerOrders(customerId)).thenReturn(totalOrders);
    CustomerDTO customerDTOResponse = customerService.getCustomerDetail(customerId);

    CustomerDTO expectedCustomerDTOResponse =
        new CustomerDTO("abcd", "male", "Ha Noi", "123132132", 3);
    Assert.assertEquals(customerDTOResponse, expectedCustomerDTOResponse);
  }

  @Test
  public void getCustomerDetailNullReturnTest() throws Exception {
    Long customerId = 1L;
    when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
    CustomerDTO customerDTOResponse = customerService.getCustomerDetail(customerId);
    Assert.assertNull(customerDTOResponse);
  }


  @Test(expected = Exception.class)
  public void getCustomerDetailThrowExceptionTestWhenGetFromDatabase() throws Exception {
    Long customerId = 1L;
    when(customerRepository.findById(customerId)).thenThrow(new Exception("Connection error"));
    customerService.getCustomerDetail(customerId);
  }

  @Test(expected = Exception.class)
  public void getCustomerDetailThrowExceptionTestWhenGetFromOrderService() throws Exception {
    Long customerId = 1L;
    when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer1));
    when(orderV1Client.getCustomerOrders(customerId))
        .thenThrow(new Exception("Failed to get total customer orders"));
    customerService.getCustomerDetail(customerId);
  }

}
