package integrationtest.externalAPI;


import com.spring.ws.dto.external.CustomerInfoBasedIpDTO;
import com.spring.ws.dto.external.CustomerLanguageDTO;
import com.spring.ws.dto.external.CustomerLocationDTO;
import com.spring.ws.http_client.IpStackClient;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 0, stubs = "classpath:/stubs/ip-stack/*.json")
@ActiveProfiles("integration-external-api-test")
@TestPropertySource(
    properties = {
        "ip-stack.ribbon.listOfServers=http://localhost:${wiremock.server.port}",
        "ip-stack.ribbon.NIWSServerListClassName=com.netflix.loadbalancer.ConfigurationBasedServerList",
        "customer-service.ip-stack.access-key=12345",
        "customer-service.ip-stack.customer-address-test-ip=116.101.100.38"
    }
)
@SpringBootTest(classes = com.spring.ws.CustomerServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IpStackExternalAPITest {

  @Autowired
  private IpStackClient ipStackClient;

  @Test
  public void TestNormalResponse() throws Exception {
    String customerIP = "116.101.100.38";
    String accessKey = "12345";
    CustomerInfoBasedIpDTO serverResponseDTO = ipStackClient
        .getCustomerInfoBasedIp(customerIP, accessKey);

    CustomerInfoBasedIpDTO expectedResponseDTO = new CustomerInfoBasedIpDTO(
        "AS", "Asia", "VN", "Vietnam", 16L, 106L, new CustomerLocationDTO(
        new ArrayList<>(Arrays.asList(new CustomerLanguageDTO("vi", "Vietnamese"))), "84"));
    Assert.assertEquals(serverResponseDTO, expectedResponseDTO);
  }


}
