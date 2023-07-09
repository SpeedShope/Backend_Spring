package com.pi.Centrale_Achat.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Delivery implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    float costDelivery;
    float totalCost;
    String adress;
    Time startDate;
    Time endDate;
    @Enumerated(EnumType.STRING)
    StatusDelivery statusDelivery;
    @Enumerated(EnumType.STRING)
    TypeDelivery typeDelivery;


    @JsonBackReference
    @OneToOne
    Order order;
}
