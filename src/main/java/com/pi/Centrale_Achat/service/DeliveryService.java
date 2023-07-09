package com.pi.Centrale_Achat.service;
import java.util.List;

import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.Delivery;
import org.springframework.security.core.userdetails.UserDetails;

public interface DeliveryService {


    void addDelivery(Delivery delivery);

    Delivery addDeliveryc (UserDetails userDetail,Delivery delivery, int id);
    Delivery updateDelivery(UserDetails userDetail,Delivery delivery);
    void deleteDelivery(UserDetails userDetail, Delivery delivery);
    Delivery getDelivery(UserDetails userDetails, int id);
    List<Delivery> getAllDeliveries(UserDetails userDetails);

    void sendDeliveryStatusSMS(UserDetails userDetails, int id);
    Delivery cost (Bill bill, Delivery delivery, int id);
}
