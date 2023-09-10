package com.pi.Centrale_Achat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
@ToString
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    Date datCmd;
    String code;



    @JoinTable(name = "oders_produts")
    @ManyToMany
    List<Product> products;


    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    Bill bill;
    @JsonIgnore
    @ManyToOne
    User user;
    @OneToOne(mappedBy = "order")
    Delivery delivery;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST})
    List<RequestClaim>requestClaims;

}
