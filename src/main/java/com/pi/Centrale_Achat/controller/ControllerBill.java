package com.pi.Centrale_Achat.controller;
import com.pi.Centrale_Achat.dto.StatistiquesFactures;
import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.BillRepo;
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

@RestController
@RequestMapping("/api/bill")
@RequiredArgsConstructor
public class ControllerBill {
    private final BillServiceImpl billService;
    private final UserRepo userRepo;
    private final BillRepo billRepo;

    @GetMapping("/get-top-facture")
    public Bill topFacture(){
        return billService.topFacture();
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Bill> getbillsUser(@AuthenticationPrincipal UserDetails userDetails){
        String currentUserName = userDetails.getUsername();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser == null) {
            return Collections.emptyList();
        } else {
            return billService.getBillsForUser(userDetails);
        }
    }
    @GetMapping("/bil")
    public List<Bill> getBill() {
        return billService.getBill();
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("add/{id}")
    public Bill addFacture(@AuthenticationPrincipal UserDetails userDetails,@RequestBody Bill b , @PathVariable("id")int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        return billService.addFacture(userDetails,b, id);
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("add/{id}/{code}")
    public Bill addFacturePromo(@AuthenticationPrincipal UserDetails userDetails,@RequestBody Bill b ,
                                @PathVariable("id")int id,@PathVariable("code")String code)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepo.findUserByUsername(currentUserName);
        if (currentUser==null) {
            System.out.println("Vous devez se connecter");
        }
        return billService.addFactureWithCodePromo(userDetails, b,id,code);

    }
    @GetMapping("/code-promo")
    public String getCode() {
        return billService.generateCode();
    }


    @GetMapping("/statistiques")
    @PreAuthorize("hasRole('ADMIN')")
    public StatistiquesFactures getStatistiquesFactures() {
        List<Bill> factures = billRepo.findAll();
        int nombreTotalFactures = factures.size();

        float montantTotalVentes = factures.stream()
                .map(Bill::getPrice)
                .reduce(0f, Float::sum);

        float montantMoyenVentes = montantTotalVentes / nombreTotalFactures;
        return new StatistiquesFactures(nombreTotalFactures, montantTotalVentes, montantMoyenVentes);
    }

}