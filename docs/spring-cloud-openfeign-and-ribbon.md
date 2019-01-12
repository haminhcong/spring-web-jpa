# Documents about spring cloud openfeign and ribbon

- https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.0.2.RELEASE/single/spring-cloud-openfeign.html#netflix-feign-starter
- https://stackoverflow.com/questions/48740775/feign-client-mapping-by-parameter
- https://dzone.com/articles/quick-guide-to-microservices-with-spring-boot-20-e


## Use spring cloud openfeign as http connector between spring cloud mircoservices and between spring cloud microservice and external webservice

Refs: 

- https://cloud.spring.io/spring-cloud-openfeign/single/spring-cloud-openfeign.html#netflix-feign-starter
- https://piotrminkowski.wordpress.com/2018/04/26/quick-guide-to-microservices-with-spring-boot-2-0-eureka-and-spring-cloud/
- https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-ribbon.html
- https://stackoverflow.com/questions/38360182/how-to-disable-eureka-lookup-on-specific-feignclient/38360581#38360581

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

### Customize Ribbon client

- https://cloud.spring.io/spring-cloud-static/Edgware.SR4/multi/multi_spring-cloud-ribbon.html
- https://www.oreilly.com/library/view/mastering-spring-cloud/9781788475433/faf8e3d2-f053-4401-bb2b-a5f0e072e80b.xhtml

Working in progress

### Feign client interceptor (need test again)

- https://github.com/spring-cloud/spring-cloud-netflix/issues/2746#issuecomment-430481739


### Use openFeign and ribbon without eureka, using kubernetes or consul service discovery instead

Refs:

- https://github.com/spring-cloud/spring-cloud-netflix/issues/1758
- https://github.com/spring-cloud/spring-cloud-netflix/issues/564
- https://spring.io/guides/gs/client-side-load-balancing/#_load_balance_across_server_instances
- https://spring.io/guides/gs/client-side-load-balancing/#_load_balance_across_server_instances

Working in this issue is in progress...

