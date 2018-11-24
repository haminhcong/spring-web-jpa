package com.spring.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

  private String userName;
  private String gender;
  private String address;
  private String phoneNumber;
  private List<OrderDTO> orders;
}
