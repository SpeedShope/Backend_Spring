package com.pi.Centrale_Achat.service;

import java.util.List;

import com.pi.Centrale_Achat.entities.Mission;

public interface missionService {
 
	 Mission AffectOrder(int userId, int orderID ) ; 
	 List<Mission> getAllMissions(); 
	 Mission getMissionByID(int id ); 
	 
}
