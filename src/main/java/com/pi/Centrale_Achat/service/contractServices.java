package com.pi.Centrale_Achat.service;

import java.util.List;

import com.pi.Centrale_Achat.entities.Contract;

public interface contractServices {

	Contract saveContractandAssigntoUserContract(Contract c , int userid) ; 
	List<Contract> getAllContracts(); 
	Contract getContractById(int id ); 
	Contract acceptContract(int id); 
	
	List<Contract> getUserContract(int userid); 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
