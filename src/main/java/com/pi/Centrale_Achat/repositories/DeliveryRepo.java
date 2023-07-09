package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.Delivery;
import com.pi.Centrale_Achat.entities.StatusDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepo extends JpaRepository<Delivery,Integer> {
    List<Delivery> findBystatusDelivery(StatusDelivery delivered);
}
