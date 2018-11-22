package com.spring.service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "order")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "customer_id")
  private String customerID;

  @Column(name = "delivery_address")
  private String deliveryAddress;

  @Column(name = "email_address")
  private String emailAddress;

  @Column(name = "purchase_date")
  @Temporal(TemporalType.DATE)
  private Date purchaseDate;

  @ManyToOne
  @JoinColumn(name = "order_status_id")
  private OrderStatus orderStatus;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true)
  private List<OrderedItem> orderedItemList = new ArrayList<>();
}
