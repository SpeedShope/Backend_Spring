package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.MvStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepo extends JpaRepository<MvStock,Integer> {
}
