package com.pi.Centrale_Achat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@ToString
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    float price;
    String image;
    int qte;
    String description;
    int minStock;
    @Temporal(TemporalType.DATE)
    Date startDateDiscount;
    @Temporal(TemporalType.DATE)
    Date endDateDiscount;
    private float discount;

    private int rating;
    private int numberOfRates = 0;
    boolean accepted;
    @JsonIgnore
    @ManyToMany(mappedBy = "products", cascade = {CascadeType.PERSIST})
    List<Order> orders;
    @JsonIgnore
    @ManyToOne
    Category category;
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    List<MvStock> stocks;
    @JsonIgnore
    @ManyToOne
    User user;
    @JsonIgnore
    @ManyToOne
    Tender tender;
    public boolean isAccepted() {
        return accepted;
    }
    @JsonIgnore
    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    List<OperatorScore> operatorScores;
    public Product(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.image = imageUrl;

    }

}
