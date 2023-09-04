package com.pi.Centrale_Achat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

}
