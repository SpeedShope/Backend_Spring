package com.pi.Centrale_Achat.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifierClient {

    @Autowired
    private SmsService smsService;

    public void sendAlert(String productName, int currentQuantity,String phoneNumber) {

        String smsRecipient = phoneNumber;

        String message = String.format("Alert: Product %s has you wanted is available with quantity %d", productName, currentQuantity);



        // Send SMS alert
        smsService.sendSMS(smsRecipient, message);
    }

}
