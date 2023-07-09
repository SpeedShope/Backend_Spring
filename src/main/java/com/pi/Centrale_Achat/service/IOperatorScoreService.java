package com.pi.Centrale_Achat.service;

import com.pi.Centrale_Achat.entities.OperatorScore;
import org.springframework.security.core.userdetails.UserDetails;

public interface IOperatorScoreService {
    public void updateOperatorScore(int operatorId);
    public void affectationoperaterscoreaproductetuser(OperatorScore operatorScore, int idoperateur, int idproduct);
}
