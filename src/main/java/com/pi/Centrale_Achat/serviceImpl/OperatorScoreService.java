package com.pi.Centrale_Achat.serviceImpl;


import com.pi.Centrale_Achat.entities.OperatorScore;
import com.pi.Centrale_Achat.entities.Product;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.OperatorScoreRepo;
import com.pi.Centrale_Achat.repositories.ProductRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.IOperatorScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorScoreService implements IOperatorScoreService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;


    @Autowired
    private OperatorScoreRepo operatorScoreRepository;
@Override
    public void updateOperatorScore(int operatorId) {
        User user = userRepo.findById(operatorId).orElse(null);
        if (user != null) {
            List<OperatorScore> operatorScores = operatorScoreRepository.findByUser_Id(user.getId());;
            Float totalScore = Float.valueOf(0);
            for (OperatorScore operatorScore : operatorScores) {
                totalScore += operatorScore.getScore();
            }
            Float averageScore = operatorScores.size() > 0 ? totalScore / operatorScores.size() : 0;
            user.setScore(averageScore);
            userRepo.save(user);
        }
    }

    @Override

    public void affectationoperaterscoreaproductetuser(OperatorScore operatorScore, int idoperateur, int idproduct) {

        OperatorScore e=operatorScoreRepository.save(operatorScore);
    User user = userRepo.findById(idoperateur).orElse(null);
        Product product = productRepo.findById(idproduct).orElse(null);

        e.setProduct(product);
        e.setUser(user);
     e=operatorScoreRepository.save(operatorScore);


    }

}
