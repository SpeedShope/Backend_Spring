package com.pi.Centrale_Achat.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pi.Centrale_Achat.entities.Contract;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.ContractRepository;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.contractServices;

import antlr.collections.List;

@Service

public class contractServiceImplementation implements contractServices {

	@Autowired 
	ContractRepository contractRepository; 
	@Autowired
    UserRepo userRepo; 
	
	@Override
	public Contract saveContractandAssigntoUserContract( Contract c , int userid) {
		User u = userRepo.findById(userid).get(); 
        c.setUser(u);
	   return  contractRepository.save(c);
	    
		
	}

}
