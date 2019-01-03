package com.spring.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HTTPErrorResponseDTO {
  private int statusCode;
  private String errorMessage;
}
