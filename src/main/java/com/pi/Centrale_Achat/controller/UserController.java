package com.pi.Centrale_Achat.controller;


import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.payload.response.MessageResponse;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.sun.mail.imap.protocol.BASE64MailboxDecoder.decode;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200" , "http://localhost:4000"}, maxAge = 3600, allowCredentials="true")

public class UserController {

    private final    UserRepo userRepository;


    private final    UserServiceImpl userService;
//

    @PutMapping("/modifieruser")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser,
                                        @RequestParam("currentPassword") String currentPassword,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findUserByUsername(currentUserName);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Vous devez se connecter"));
        }
        User savedUser = userService.updateUser(userDetails, updatedUser, currentPassword);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/MyProfile")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findUserByUsername(currentUserName);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Vous devez se connecter"));
        }
        User getUser = userService.getMyProfile(userDetails);
        return ResponseEntity.ok(getUser);
    }

@PutMapping("/ChangeUserRole/{id}")
	 User changeUserRole(@PathVariable("id") int id) {
		 return userService.changeUserRole(id);
		 
	 }


}
