package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.ResponseClaim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseClaimRepo extends JpaRepository<ResponseClaim,Integer> {
}
