package com.pi.Centrale_Achat.controller;
import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.serviceImpl.BillServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/bill")
@RequiredArgsConstructor
public class ControllerBill {
    private final BillServiceImpl billService;
    private final UserRepo userRepo;

    @GetMapping("/get-top-facture")
    public Bill topFacture(){
        return billService.topFacture();
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/getAll")
    public List<Bill> getbillsUser(@AuthenticationPrincipal UserDetails userDetails){
        String currentUserName = userDetails.getUsername();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser == null) {
            return Collections.emptyList();
        } else {
            return billService.getBillsForUser(userDetails);
        }
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("add/{id}")
    public Bill add(@AuthenticationPrincipal UserDetails userDetails,@RequestBody Bill b , @PathVariable("id")int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        return billService.addFacture(userDetails,b, id);

    }




}
