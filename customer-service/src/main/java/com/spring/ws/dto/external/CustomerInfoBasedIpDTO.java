package com.spring.ws.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfoBasedIpDTO {

  @JsonProperty("continent_code")
  private String continentCode;

  @JsonProperty("continent_name")
  private String continentName;

  @JsonProperty("country_code")
  private String countryCode;

  @JsonProperty("country_name")
  private String countryName;

  @JsonProperty("latitude")
  private Long latitude;

  @JsonProperty("longitude")
  private Long longitude;

  @JsonProperty("location")
  private CustomerLocationDTO locationInfo;

}
