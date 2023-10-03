package com.pi.Centrale_Achat.service;


import com.pi.Centrale_Achat.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {


     User updateUser(UserDetails userDetails, User updatedUser, String currentPassword);
     User getMyProfile(UserDetails userDetails);

     List <User> getAllUsers();
     List<User> getAllDeliveryAgents(); 
     
     User findUserByUsername(String username); 
     public User ValidateAccount(String username) ; 
     public  User changeUserRole(int id);  
     
}
