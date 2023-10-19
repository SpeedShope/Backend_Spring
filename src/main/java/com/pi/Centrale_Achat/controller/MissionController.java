package com.pi.Centrale_Achat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi.Centrale_Achat.entities.Mission;
import com.pi.Centrale_Achat.serviceImpl.MissionServImplementation;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mission")
public class MissionController  {

	@Autowired
	MissionServImplementation msserv; 
	
	@PostMapping("/AffectOrderToDeliveryAgent/{iduser}/{idorder}")
	public  Mission AffectOrderToDeliveryAgent(@PathVariable("iduser") int iduser,@PathVariable("idorder") int idorder ) {
		return msserv.AffectOrder(iduser, idorder);
	}
	
	@GetMapping("/getAllMissions")
	public List<Mission> getAllMissions(){
		return msserv.getAllMissions(); 
	}
	@GetMapping("/getMissionsbyid/{id}")
	public Mission getMissionByID(@PathVariable("id") int id){
		return msserv.getMissionByID(id); 
	}
	
}
