# Documents about spring cloud openfeign and ribbon

- https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.0.2.RELEASE/single/spring-cloud-openfeign.html#netflix-feign-starter
- https://stackoverflow.com/questions/48740775/feign-client-mapping-by-parameter
- https://dzone.com/articles/quick-guide-to-microservices-with-spring-boot-20-e


## Use spring cloud openfeign as http connector between spring cloud mircoservices and between spring cloud microservice and external webservice


### Between spring cloud micro services 

- Use micro service name to connect.

```java
// customer-service code


package com.spring.ws.http_client;

import com.spring.ws.dto.TotalOrdersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("order-service")
public interface OrderV1Client {

  @RequestMapping(value = "/v1/customer-total-orders", method = RequestMethod.GET)
  TotalOrdersDTO getCustomerOrders(@RequestParam("customerID") Long customerID) throws Exception;
}

```

We can config for feign client like this:

```yaml
feign:
  client:
    config:
      order-service:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: full
              
```

### Between microservice and external webservice 

- Set feign name for external web service, in this case is `ip-stack`

```java
package com.spring.ws.http_client;

import com.spring.ws.dto.external.CustomerInfoBasedIpDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ip-stack")
public interface IpStackClient {
  @RequestMapping(value = "/{customerIP}", method = RequestMethod.GET)
  CustomerInfoBasedIpDTO getCustomerOrders(
      @PathVariable("customerIP") String customerIP,
      @RequestParam("access_key") String accessKey) throws Exception;
}

```

Then set config for external web service ribbon config

```yaml

ip-stack:
  ribbon:
    NIWSServerListClassName: com.netflix.loadbalancer.ConfigurationBasedServerList
    listOfServers: http://api.ipstack.com
```


