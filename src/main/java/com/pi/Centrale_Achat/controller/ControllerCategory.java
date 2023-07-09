package com.pi.Centrale_Achat.controller;

import com.pi.Centrale_Achat.entities.Category;

import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.CategoryRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;


import com.pi.Centrale_Achat.serviceImpl.CategoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "http://localhost:4200")

@RequiredArgsConstructor
public class ControllerCategory {
    private final CategoryImpl categoryservice;


    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo ;


    @GetMapping("/")
    public ResponseEntity<?> index(){

        return ResponseEntity.ok("hiiiiiiiiii");
    }
    @GetMapping("/getAll")

    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<?> index_Category(@AuthenticationPrincipal UserDetails userDetails){
        String currentUserName = userDetails.getUsername();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser == null) {
            // Handle error case where user is not found
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND) ;
        } else
            return ResponseEntity.ok(categoryservice.show_All(userDetails));
    }
    @PostMapping("/saveCategorie")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<?> saveCategorie(@AuthenticationPrincipal UserDetails userDetails,@RequestBody Category ca){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        categoryservice.saveCategorie(userDetails,ca);
        return new ResponseEntity<>("Category ajouter avec success", HttpStatus.OK);
    }
    @PutMapping("/modifierCategorie/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<?>  modifierCategorie(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody Category ca,@PathVariable("id") int id){
       // Category category = categoryRepo.findById(id)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        Category cat = categoryRepo.findById(id).orElse(null);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
                    if (!(currentUser.getId()==cat.getUser().getId())){
                return new ResponseEntity<>("erreur", HttpStatus.FORBIDDEN);
            }
            else {
                categoryservice.modifierCategorie(userDetails,ca,id);
                return new ResponseEntity<>("Category modifier avec success", HttpStatus.OK);
            }
        }

    @DeleteMapping("/delete/{idc}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetails userDetails,@PathVariable int idc){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();
    Category category = categoryRepo.findById(idc).orElse(null);
     if ( category != null && (currentUsername.equals(category.getUser().getUsername()))) {
        categoryservice.delete(userDetails,idc);
         return new ResponseEntity<>("succes ", HttpStatus.OK);
    } else {

         return new ResponseEntity<>("Not found ", HttpStatus.NOT_FOUND);

    }

    }

}
