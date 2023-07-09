package com.pi.Centrale_Achat.entities;
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
public class RequestClaim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idRequestClaim;
    @Enumerated(EnumType.STRING)
    TypeClaim typeClaim;
    int ref;
    Date dateCreation;
    String Summary;
    Status satus;
    String ClientRequester;
    @ManyToOne
    Order order;
    @OneToMany(mappedBy = "requestClaim",cascade = {CascadeType.PERSIST})
    List<ResponseClaim>responseClaims;
    @ManyToOne
    User user;
}
