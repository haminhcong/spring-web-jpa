package com.spring.ws.configuration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("customer-service")
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerServiceProperties {

    private IpStackProperties ipStack;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IpStackProperties{

        @NotNull
        @NotEmpty
        private String accessKey;

        @NotNull
        @NotEmpty
        private String customerAddressTestIP;
    }
}
