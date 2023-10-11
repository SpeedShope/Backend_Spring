package com.pi.Centrale_Achat.serviceImpl;

import com.pi.Centrale_Achat.entities.ERole;
import com.pi.Centrale_Achat.entities.Role;
import com.pi.Centrale_Achat.entities.Tender;
import com.pi.Centrale_Achat.entities.User;
import com.pi.Centrale_Achat.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepo userRepository;

    public void sendEmailToAllUsers(Tender tender) throws MessagingException, javax.mail.MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("mazen.slama@esprit.tn");
        helper.setSubject("New Tender Created: " + tender.getName());

        // get all users
        List<User> users = userRepository.findAll();

        String emailBody = "A new tender has been created with the following details: <br>";
        emailBody += "<strong>Tender Name:</strong> " + tender.getName() + "<br>";
        emailBody += "<strong>Description:</strong> " + tender.getDescription() + "<br>";
        emailBody += "<strong>Quantity:</strong> " + tender.getQuantity() + "<br>";
        emailBody += "<strong>Marque:</strong> " + tender.getBrand() + "<br>";

        emailBody += "<br><br>Regards, <br>SpeedShop Team";

        helper.setText(emailBody, true);

        // send email to all users
        for (User user : users) {
            for (Role role :user.getRoles() ) {
                if(role.getName()==ERole.ROLE_SUPPLIER){
                    helper.setTo(user.getEmail());
                javaMailSender.send(message);
                     }
            }

            }

    }

}

