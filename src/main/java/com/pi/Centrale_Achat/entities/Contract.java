package com.pi.Centrale_Achat.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.*;
import lombok.Setter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contract implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int contractid ; 
	int contractduration ; 
	@Temporal(TemporalType.DATE)
	Date contractdatedeb; 
	String cin ;
	boolean hasCar;
    String typeday;
    String carModel ; 
    String carType; 
	@ManyToOne
	User user ; 
	boolean signature ;
	
	boolean acceptstatus; 
	
	
	
	

}
