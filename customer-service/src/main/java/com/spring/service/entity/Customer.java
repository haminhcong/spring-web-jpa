package com.spring.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "account_name",unique = true,nullable = false)
  private String accountName;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "password")
  private String password;

  @Column(name = "gender")
  private String gender;

  @Column(name = "address")
  private String address;

  @Column(name = "phone_number")
  private String phoneNumber;

}
