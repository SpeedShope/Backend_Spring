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

	@Override
	public java.util.List<Contract> getAllContracts() {
		return contractRepository.findAll();
	}

	@Override
	public Contract getContractById(int id) {
		return contractRepository.findById(id).get();
	}

	@Override
	public Contract acceptContract(int id) {
		Contract c =contractRepository.findById(id).get(); 
		c.setAcceptstatus(true);
		return 		contractRepository.save(c); 
		
	}

	@Override
	public java.util.List<Contract> getUserContract(int userid) {
		User u = userRepo.findById(userid).get();
		return u.getContracts();
	}
	
}
