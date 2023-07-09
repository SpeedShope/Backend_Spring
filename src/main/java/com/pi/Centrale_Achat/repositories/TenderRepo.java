package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.Tender;
import com.pi.Centrale_Achat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenderRepo extends JpaRepository<Tender,Integer> {
    List<Tender> findByUser(User user);

}
