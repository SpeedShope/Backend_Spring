package com.pi.Centrale_Achat.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    @Autowired
    private MailService emailService;

    @Autowired
    private SmsService smsService;

    public void sendAlert(String productName, int currentQuantity,String phoneNumber) {
        String emailRecipient = "test.esprit10@gmail.com";
        String smsRecipient = phoneNumber;

        String message = String.format("Alert: Product %s has reached the minimum quantity of %d", productName, currentQuantity);

        // Send email alert
        emailService.sendEmail(emailRecipient, "Product Alert", message);

        // Send SMS alert
        smsService.sendSMS(smsRecipient, message);
    }
}