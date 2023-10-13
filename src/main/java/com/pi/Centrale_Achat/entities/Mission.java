package com.pi.Centrale_Achat.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mission  implements Serializable{
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	int id ; 
	 @ManyToOne
	 User user; 
	@OneToOne
	Order order ; 
	boolean status; 

}
