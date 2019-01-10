package com.spring.ws.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CustomerServiceProperties.class)
public class CustomerServiceConfiguration {

}
