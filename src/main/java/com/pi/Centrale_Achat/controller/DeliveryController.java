package com.pi.Centrale_Achat.controller;


import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.Delivery;
import com.pi.Centrale_Achat.entities.Order;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.BillRepo;
import com.pi.Centrale_Achat.repositories.DeliveryRepo;
import com.pi.Centrale_Achat.repositories.OrderRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    BillRepo billRepository;
    @Autowired
    OrderRepo orderRepository;
    @Autowired
    DeliveryRepo deliveryRepository;
    @Autowired
    private  UserRepo userRepo;


    @GetMapping("/send-sms/{id}")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<String> sendDeliveryStatusSMS(@AuthenticationPrincipal UserDetails userDetail,@PathVariable("id") int id) {
        deliveryService.sendDeliveryStatusSMS(userDetail,id);
        return new ResponseEntity<>(HttpStatus.OK);

    }




    @PostMapping("/add")
    public void addDelivery(@RequestBody Delivery delivery){

        deliveryService.addDelivery(delivery);
    }


    @PostMapping("/addd/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Delivery addDeliveryc(@AuthenticationPrincipal UserDetails userDetail,@RequestBody Delivery delivery, @PathVariable("id")int id ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }

        return deliveryService.addDeliveryc(userDetail,delivery,id);
    }



    @PutMapping("/update")
    @PreAuthorize("hasRole('DELIVERY')")
    public Delivery  updateDelivery(@AuthenticationPrincipal UserDetails userDetail, @RequestBody Delivery  delivery, @RequestParam int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        delivery.setId(id);
        return deliveryService.updateDelivery(userDetail, delivery);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('DELIVERY')")
    public void deleteDelivery(@AuthenticationPrincipal UserDetails userDetail, @PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        Delivery delivery= deliveryService.getDelivery(userDetail,id);
        deliveryService.deleteDelivery(userDetail,delivery);
    }

    @GetMapping("/get-delivery/{id}")
    @PreAuthorize("hasRole('DELIVERY')")
    public Delivery getDelivery(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") int id) {
        String currentUserName = userDetails.getUsername();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser == null) {
            // Handle error case where user is not found
            return (Delivery) Collections.emptyList();
        } else {
            return deliveryService.getDelivery(userDetails,id);
        }
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('DELIVERY')")
    public List<Delivery> getAll(@AuthenticationPrincipal UserDetails userDetails){
        String currentUserName = userDetails.getUsername();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser == null) {
            // Handle error case where user is not found
            return Collections.emptyList();
        } else {
            return deliveryService.getAllDeliveries(userDetails);
        }
    }



}
