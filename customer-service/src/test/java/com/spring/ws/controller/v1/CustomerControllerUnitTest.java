package com.spring.ws.controller.v1;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.spring.ws.dto.response.CustomerDTO;
import com.spring.ws.dto.response.CustomerListResponseDTO;
import com.spring.ws.dto.response.HTTPErrorResponseDTO;
import com.spring.ws.entity.Customer;
import com.spring.ws.exceptions.CustomerServiceDatabaseErrorException;
import com.spring.ws.exceptions.ExternalServiceErrorException;
import com.spring.ws.service.CustomerService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
@ActiveProfiles("controller-unit-test")
public class CustomerControllerUnitTest {

  @MockBean
  CustomerService customerService;

  @Autowired
  private MockMvc mockMvc;

  private Gson gson = new Gson();

  private List<Customer> customerList = new ArrayList<>();
  private Customer customer1;
  private Customer customer2;

  @Before
  public void setUp() {
    customer1 = new Customer(1L, "ddd", "abcd", "bcd", "male", "Ha Noi", "123132132");
    customer2 = new Customer(2L, "def", "cab", "bcd", "female", "Ho Chi Minh City", "1231545243");
    customerList.add(customer1);
    customerList.add(customer2);
  }

  @Test
  public void getAllCustomersResponseNormalTest() throws Exception {
    when(customerService.getCustomerList()).thenReturn(customerList);
    MvcResult response = this.mockMvc
        .perform(get("/v1/customers"))
        .andExpect(status().isOk())
        .andReturn();
    String responseString = response.getResponse().getContentAsString();
    CustomerListResponseDTO actualResponse =
        gson.fromJson(responseString, CustomerListResponseDTO.class);
    CustomerListResponseDTO expectedResponse = new CustomerListResponseDTO(customerList);
    Assert.assertEquals(actualResponse, expectedResponse);
  }


  @Test
  public void getAllCustomersThrowExceptionTest() throws Exception {
    when(customerService.getCustomerList()).thenThrow(new Exception("database error"));
    MvcResult response = this.mockMvc
        .perform(get("/v1/customers"))
        .andExpect(status().isInternalServerError())
        .andReturn();
    String responseString = response.getResponse().getContentAsString();
    HTTPErrorResponseDTO actualResponse =
        gson.fromJson(responseString, HTTPErrorResponseDTO.class);
    Assert.assertEquals(
        actualResponse.getErrorMessage(),
        "Fail to get customer list from database");
    Assert.assertEquals(actualResponse.getStatusCode(), 500);

  }

  @Test
  public void getCustomerInfoNormalTest() throws Exception {
    Long customerId = 2L;
    CustomerDTO customerDetailInfo = new CustomerDTO(
        "customer2", "male", "customer2Address", "123456", 5
    );

    when(customerService.getCustomerDetail(customerId)).thenReturn(customerDetailInfo);

    MvcResult response = this.mockMvc
        .perform(
            get("/v1/customers")
                .param("id", customerId.toString()))
        .andExpect(status().isOk())
        .andReturn();

    CustomerDTO actualResponse =
        gson.fromJson(response.getResponse().getContentAsString(), CustomerDTO.class);
    Assert.assertEquals(actualResponse, customerDetailInfo);
  }

  @Test
  public void getCustomerInfoNotFoundTest() throws Exception{
    Long customerId =  2L;
    when(customerService.getCustomerDetail(customerId)).thenReturn(null);
    MvcResult response = this.mockMvc
        .perform(
            get("/v1/customers")
                .param("id", customerId.toString()))
        .andExpect(status().isNotFound())
        .andReturn();
    String expectedMsgResponse = "Customer " + customerId.toString() + " not found!";
    int expectedStatusCode =  404;
    HTTPErrorResponseDTO actualResponse =
        gson.fromJson(response.getResponse().getContentAsString(), HTTPErrorResponseDTO.class);
    Assert.assertEquals(expectedMsgResponse, actualResponse.getErrorMessage());
    Assert.assertEquals(expectedStatusCode, actualResponse.getStatusCode());
  }

  @Test
  public void getCustomerInfoInternalErrorTest() throws  Exception{
    Long customerId =  2L;
    String exceptionMessage =  "database error or order service error";
    when(customerService.getCustomerDetail(customerId)).thenThrow(
        new CustomerServiceDatabaseErrorException("database error or order service error"));
    MvcResult response = this.mockMvc
        .perform(
            get("/v1/customers")
                .param("id", customerId.toString()))
        .andExpect(status().isInternalServerError())
        .andReturn();
    String expectedMsgResponse = "Fail to get customer "+ customerId + ". Reason: " + exceptionMessage;
    int expectedStatusCode =  500;
    HTTPErrorResponseDTO actualResponse =
        gson.fromJson(response.getResponse().getContentAsString(), HTTPErrorResponseDTO.class);
    Assert.assertEquals(expectedMsgResponse, actualResponse.getErrorMessage());
    Assert.assertEquals(expectedStatusCode, actualResponse.getStatusCode());

  }

}
