package com.spring.webapp.entity;

import com.spring.webapp.data_type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "address")
    private String address;
    @Type(type = "com.spring.webapp.data_type.PhoneNumberType")
    @Column(name = "phone_number")
    private PhoneNumber phoneNumber;
}
