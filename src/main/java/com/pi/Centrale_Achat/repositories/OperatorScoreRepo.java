package com.pi.Centrale_Achat.repositories;


import com.pi.Centrale_Achat.entities.OperatorScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface OperatorScoreRepo extends JpaRepository<OperatorScore,Integer> {
    List<OperatorScore> findByUser_Id(int operatorId);
}
