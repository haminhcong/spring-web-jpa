package com.spring.ws.dto.external;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLanguageDTO {

  @JsonProperty("code")
  private String languageCode;

  @JsonProperty("name")
  private String languageName;

}
