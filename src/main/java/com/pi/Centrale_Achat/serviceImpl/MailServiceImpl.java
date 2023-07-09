package com.pi.Centrale_Achat.serviceImpl;
import com.pi.Centrale_Achat.dto.Email;
import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.repositories.BillRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class MailServiceImpl {
    private final JavaMailSender mailSender;
    private final BillRepo billRepo;



    public void sendEmail(Email email,int idfacture ){

        Bill b = billRepo.findById(idfacture).get();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("test.esprit10@gmail.com");
        simpleMailMessage.setTo(b.getOrder().getUser().getEmail());
        simpleMailMessage.setSubject(email.getSubjuct());
        simpleMailMessage.setText("votre facture "+"\n code facture:"+b.getCode()+"\n le prix totale:"+
                b.getPrice()+"\n date facture :"+b.getDateFacture());
        this.mailSender.send(simpleMailMessage);

    }




}
