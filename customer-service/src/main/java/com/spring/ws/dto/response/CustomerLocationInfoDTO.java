package com.spring.ws.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLocationInfoDTO {
  private String countryName;
  private String countryCode;
  private String languageCode;
  private String callingCode;
  private Long latitude;
  private Long longitude;
}
