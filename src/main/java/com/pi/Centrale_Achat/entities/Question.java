package com.pi.Centrale_Achat.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String question;
    private String proposition1;
    private String proposition2;
    private String proposition3;
    private String reponse;
    @ManyToOne
    Feedback feedback;

}
