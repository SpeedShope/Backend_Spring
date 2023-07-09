package com.pi.Centrale_Achat.controller;


import com.pi.Centrale_Achat.entities.ERole;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200" , "http://localhost:4000"}, maxAge = 3600, allowCredentials="true")
public class AdminController {

    private final UserServiceImpl userService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }



    @GetMapping("/statisque")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<ERole, Integer> getstatistique() {
        return userService.getstatistique();
    }


}
