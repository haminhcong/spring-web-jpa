package integrationtest.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.spring.ws.entity.Customer;
import com.spring.ws.repository.CustomerRepository;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = RepositoryConfiguration.class)
@ActiveProfiles("integration-repository-test")
public class CustomerRepositoryTest {

  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private CustomerRepository customerRepository;

  @Test
  public void testCustomerRepository() throws Exception {
    Customer customer1 = new Customer("ddd", "abcd", "bcd",
        "male", "Ha Noi", "123132132");
    Customer customer2 = new Customer("def", "cab", "bcd",
        "female", "Ho Chi Minh City", "1231545243");
    Customer customer3 = new Customer("ghi", "ghi", "ghi",
        "female", "Ho Chi Minh City", "987654");

    this.testEntityManager.persist(customer1);
    this.testEntityManager.persist(customer2);
    this.testEntityManager.persist(customer3);

    List<Customer> customerList = this.customerRepository.findAll();
    assertThat(customerList.size()).isEqualTo(3);

    Long checkCustomerID = customer2.getId();

    Customer existCustomer = this.customerRepository.findById(checkCustomerID).orElse(null);
    assertThat(existCustomer).isEqualTo(customer2);

    Customer nonExistCustomer = this.customerRepository.findById(1552L).orElse(null);
    assertThat(nonExistCustomer).isNull();

  }
}
