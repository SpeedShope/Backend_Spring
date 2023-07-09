package com.pi.Centrale_Achat.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MvStock implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private Integer quantiteMvt ;
    private Date dateMvt ;
    @Enumerated(EnumType.STRING)
    private TypeMvStock typeMvt ;

    @ManyToOne
    private Product product;

}