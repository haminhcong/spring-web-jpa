package com.spring.ws.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLocationDTO {

  @JsonProperty("languages")
  private List<CustomerLanguageDTO> languages;

  @JsonProperty("calling_code")
  private String callingCode;


}
