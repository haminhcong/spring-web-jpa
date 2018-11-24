package com.spring.service.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "order_status")
@AllArgsConstructor
public class OrderStatus {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "order_status_code", unique = true)
  private String orderStatusCode;

  @Column(name = "description")
  private String description;

  @OneToMany(
      cascade = CascadeType.MERGE,
      orphanRemoval = true,
      mappedBy = "orderStatus"
  )
  private List<Order> orderList = new ArrayList<>();

}
