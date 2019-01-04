package com.spring.ws.dto;

import com.spring.ws.entity.Customer;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListResponseDTO {
  private List<Customer> customerList;
}
