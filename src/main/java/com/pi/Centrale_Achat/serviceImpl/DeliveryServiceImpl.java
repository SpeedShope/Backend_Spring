package com.pi.Centrale_Achat.serviceImpl;

import com.pi.Centrale_Achat.entities.*;
import com.pi.Centrale_Achat.repositories.BillRepo;
import com.pi.Centrale_Achat.repositories.DeliveryRepo;
import com.pi.Centrale_Achat.repositories.OrderRepo;
import com.pi.Centrale_Achat.repositories.UserRepo;
import com.pi.Centrale_Achat.service.DeliveryService;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepo deliveryRepository;
    private final OrderRepo orderRepository;
    private final UserRepo userRepository;
    private final BillRepo billRepo;



    @Override
    public void addDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }



    public Delivery addDeliveryc(@AuthenticationPrincipal UserDetails userDetails,Delivery delivery , int id ) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepository.findUserByUsername(currentUser);

        Bill bill = billRepo.findById(id).orElse(null);
        int orderId = bill.getOrder().getId();
        Order order = orderRepository.findById(orderId).orElse(null);
        delivery.setOrder(order);

        delivery.setTotalCost(bill.getPrice());

        cost (bill, delivery, id);
        delivery.getOrder().setUser(user1);
        return deliveryRepository.save(delivery);

    }
    



    @Override
    public Delivery updateDelivery(@AuthenticationPrincipal UserDetails userDetails, Delivery delivery) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepository.findUserByUsername(currentUser);

        return deliveryRepository.save(delivery);
    }
    @Override
    public void deleteDelivery(@AuthenticationPrincipal UserDetails userDetails,Delivery delivery) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepository.findUserByUsername(currentUser);

            deliveryRepository.delete(delivery);


    }

    @Override
    public List<Delivery> getAllDeliveries(@AuthenticationPrincipal UserDetails userDetails){
        String currentUser = userDetails.getUsername();
        User user1 = userRepository.findUserByUsername(currentUser);
        return (List<Delivery>) deliveryRepository.findAll();
    }
    @Override
    public Delivery getDelivery(@AuthenticationPrincipal UserDetails userDetails, int id) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepository.findUserByUsername(currentUser);
        return deliveryRepository.findById(id).orElse(null);
    }


    public Delivery cost (Bill bill, Delivery delivery,int id) {
        if (bill.getId() == id) {
            if (delivery.getTypeDelivery().toString().equals("SameDay")) {
                delivery.setCostDelivery(80);}
            if (delivery.getTypeDelivery().toString().equals("Normal")) {
                delivery.setCostDelivery(10);
            }

            if (delivery.getTotalCost() >= 100 && delivery.getTypeDelivery().toString().equals("Normal") ) {

                delivery.setCostDelivery(0);
            }
            else if (delivery.getTotalCost() >= 100 && delivery.getTypeDelivery().toString().equals("SameDay")) {
                delivery.setCostDelivery(delivery.getCostDelivery() * 80 / 100);
            }
            delivery.setTotalCost(bill.getPrice()+ delivery.getCostDelivery());
        }
        return deliveryRepository.save(delivery);
    }




    // Your Twilio Account SID and Auth Token from twilio.com/console
    private static final String ACCOUNT_SID = "AC0d8b4641d8ed03307a136b71f62989de";
    private static final String AUTH_TOKEN = "5fc47ff1d5b4aae765874dccd2667334";
    private static final String TWILIO_PHONE_NUMBER = "+15673131379";

    @Override
    public void sendDeliveryStatusSMS(@AuthenticationPrincipal UserDetails userDetails,int id) {
        String currentUser = userDetails.getUsername();
        User user1 = userRepository.findUserByUsername(currentUser);
        // Find all deliveries with status "delivered"

        List<Delivery> deliveredDeliveries = deliveryRepository.findBystatusDelivery(StatusDelivery.valueOf("delivered"));

        // Loop through each delivery and send an SMS to the user's phone number
        for (Delivery deliveredDelivery : deliveredDeliveries) {
            if (deliveredDelivery.getId()==id) {
                Order order = orderRepository.findById(deliveredDelivery.getOrder().getId()).orElse(null);
                if (order != null) {

                    User user = userRepository.findById(order.getUser().getId()).orElse(null);
                    if (user != null && user.getNumTel() != null) {
                        String phoneNumber = user.getNumTel();
                        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                        Message message = Message.creator(
                                new PhoneNumber(phoneNumber),
                                new PhoneNumber(TWILIO_PHONE_NUMBER),
                                "Your order has been delivered!"
                        ).create();
                    }
                }
            }
        }
    }
}



