package com.pi.Centrale_Achat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    Date datCmd;
    String code;
    int qte;
    @JsonIgnore
    @JoinTable(name = "oders_produts")
    @ManyToMany()
    List<Product>products;
    @JsonIgnore
    @OneToOne(mappedBy = "order", cascade = {CascadeType.PERSIST,CascadeType.REMOVE} )
    Bill bill;
    @JsonIgnore
    @ManyToOne
    User user;
    @OneToOne(mappedBy = "order")
    Delivery delivery;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST})
    List<RequestClaim>requestClaims;

}
