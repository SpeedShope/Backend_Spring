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
public class Tender implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String description;
    String quantity;
    String image;
    String brand;
    String dateTender;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "tender",cascade = {CascadeType.PERSIST})
    List<Comment>comments;

    @OneToMany(mappedBy = "tender",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    List<Product>products;
}
