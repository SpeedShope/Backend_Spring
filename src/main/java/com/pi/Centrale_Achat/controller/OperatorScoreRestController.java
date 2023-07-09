package com.pi.Centrale_Achat.controller;


import com.pi.Centrale_Achat.entities.OperatorScore;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.IOperatorScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/OperatorScore")
public class OperatorScoreRestController {
    @Autowired
    IOperatorScoreService iOperatorScoreService;
    @Autowired
    private UserRepo userRepo;

    @PutMapping("/update/{id}")
    public void updateOperatorScore( @PathVariable("id") int operatorId) {

         iOperatorScoreService.updateOperatorScore(operatorId);
    }
    @PutMapping("assignop/{idoperateur}/{idproduct}")
    public void assignoperatorscoreProduct(@RequestBody OperatorScore operatorScore, @PathVariable("idoperateur")  Integer idoperateur, @PathVariable("idproduct")Integer idproduct)
    {
        iOperatorScoreService.affectationoperaterscoreaproductetuser(operatorScore,idoperateur,idproduct);

        iOperatorScoreService.updateOperatorScore(idoperateur);
    }


}
