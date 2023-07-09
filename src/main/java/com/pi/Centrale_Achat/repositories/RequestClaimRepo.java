package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.RequestClaim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestClaimRepo extends JpaRepository<RequestClaim,Integer> {
}
