package componenttest.v1;

import static org.mockito.Mockito.when;

import com.spring.ws.dto.response.CustomerDTO;
import com.spring.ws.dto.response.CustomerListResponseDTO;
import com.spring.ws.dto.external.TotalOrdersDTO;
import com.spring.ws.entity.Customer;
import com.spring.ws.http_client.OrderV1Client;
import com.spring.ws.repository.CustomerRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.spring.ws.CustomerServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("component-test")
public class CustomerApiComponentTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private OrderV1Client orderV1Client;

  @Autowired
  private CustomerRepository customerRepository;

  private List<Customer> customerList = new ArrayList<>();

  @Before
  public void setUp() {
    Customer customer1 = new Customer(1L, "ddd", "abcd", "bcd", "male", "Ha Noi", "123132132");
    Customer customer2 = new Customer(2L, "def", "cab", "bcd", "female", "Ho Chi Minh City",
        "1231545243");
    customerList.add(customer1);
    customerList.add(customer2);
    customerRepository.saveAll(customerList);
  }

  @Test
  public void getCustomerListNormalTest() {
    ResponseEntity<CustomerListResponseDTO> customerListRespDTO =
        restTemplate.getForEntity("/v1/customers", CustomerListResponseDTO.class);
    Assert.assertEquals(customerListRespDTO.getStatusCode(), HttpStatus.OK);
    Assert.assertNotNull(customerListRespDTO.getBody());
    Assert.assertEquals(customerListRespDTO.getBody().getCustomerList(), customerList);
  }

  @Test
  public void getCustomerDetailNormalTest() throws Exception {

    Long customerID = 2L;
    TotalOrdersDTO totalOrders = new TotalOrdersDTO(3);
    when(orderV1Client.getCustomerOrders(customerID)).thenReturn(totalOrders);

    String customerDetailApiUrl = "/v1/customers";
    UriComponentsBuilder builder = UriComponentsBuilder
        .fromUriString(customerDetailApiUrl)
        .queryParam("id", customerID);

    ResponseEntity<CustomerDTO> customerDTOResponseEntity =
        restTemplate.getForEntity(builder.toUriString(), CustomerDTO.class);

    Assert.assertEquals(customerDTOResponseEntity.getStatusCode(), HttpStatus.OK);
    CustomerDTO customerDTOResponse = customerDTOResponseEntity.getBody();

    CustomerDTO customerDTOExpected = new CustomerDTO(
        "cab", "female", "Ho Chi Minh City", "1231545243", 3);
    Assert.assertEquals(customerDTOExpected, customerDTOResponse);

  }


}
