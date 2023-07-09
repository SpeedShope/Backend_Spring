package com.pi.Centrale_Achat.controller;
import com.pi.Centrale_Achat.entities.Order;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.OrderRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.serviceImpl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class ControllerOrder {

    private final UserRepo userRepo;

    private final OrderServiceImpl orderService;

    private final OrderRepo orderRepo;


    @PostMapping("/add/{idP}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Order add22(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Order o, @PathVariable("idP") int idP) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        return orderService.ajouter(userDetails , o , idP);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public void delete(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("id") int id) {
        // Get the current user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Get the order by id
        Order order = orderRepo.findById(id).orElse(null);

        // Check if the current user is authorized to delete the order
        if (order != null && (currentUsername.equals(order.getUser().getUsername()))) {
            orderService.delete(userDetails,id);
        } else {
            throw new AccessDeniedException("You are not authorized to delete this order");
        }
    }

        @GetMapping("/count/{d1}/{d2}")
        @PreAuthorize("hasRole('CUSTOMER')")
    public int countCmd(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("d1") @DateTimeFormat(pattern = "yyyy-MM-dd") Date d1,
                        @PathVariable("d2") @DateTimeFormat(pattern = "yyyy-MM-dd") Date d2) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        int a = 0;
        List<Order>orderList=orderRepo.findAll();
        for (Order o : orderList){
            if (currentUsername.equals(o.getUser().getUsername())){
                a=  orderService.countCmdBetweenToDate(userDetails,d1, d2);
            }
        }
        return a;
    }
    @GetMapping("/get/{d}")
    public Order getOrderByDate(@PathVariable("d") @DateTimeFormat(pattern = "yyyy-MM-dd") Date d) {
        return orderService.findOrderByDate(d);
    }


    @GetMapping("/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Order> getOrdersForCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        String currentUserName = userDetails.getUsername();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser == null) {
            // Handle error case where user is not found
            return Collections.emptyList();
        } else {
            return orderService.getOrdersForUser(currentUser);
        }
    }

}
