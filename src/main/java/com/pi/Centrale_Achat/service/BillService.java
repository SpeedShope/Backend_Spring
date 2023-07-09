package com.pi.Centrale_Achat.service;

import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface BillService {
    Bill addFacture(UserDetails userDetails, Bill b, int id);
    float calculeFacture(int idCmd);
    List<Bill> getBillsForUser(UserDetails userDetails);

    Bill topFacture();
}
