package com.pi.Centrale_Achat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi.Centrale_Achat.entities.Contract;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.RoleeRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.security.jwt.JwtUtils;
import com.pi.Centrale_Achat.serviceImpl.contractServiceImplementation;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contracts/")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4000"}, maxAge = 3600, allowCredentials = "true")
public class ContractController {
	 @Autowired
	 contractServiceImplementation contractserv ;  
	
	 @PostMapping("/saveContractAndAssignUser/{id}")
	public Contract saveContractandAssigntoUserContract(@RequestBody Contract c , @PathVariable("id") int userid) {            
		  contractserv.saveContractandAssigntoUserContract(c, userid);
		  return contractserv.saveContractandAssigntoUserContract(c,userid);

	}
	 
	 @GetMapping("getAllContracts")
		public List<Contract> getAllContracts() {
			return contractserv.getAllContracts();
		}
	 
	 @GetMapping("getcontractByid/{id}")
	  Contract findContract(@PathVariable("id") int id ) {
		 return contractserv.getContractById(id);
	 }
	 @PutMapping("acceptContract/{id}")
		public Contract acceptContract(@PathVariable("id") int id) {
			return contractserv.acceptContract(id);
		 
		}
	 @GetMapping("getUserContracts/{id}")
		public java.util.List<Contract> getUserContract(@PathVariable("id") int userid) {
		 return contractserv.getUserContract(userid);
	 }
}
