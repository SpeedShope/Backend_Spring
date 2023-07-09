package com.pi.Centrale_Achat.controller;
import com.pi.Centrale_Achat.dto.Email;
import com.pi.Centrale_Achat.serviceImpl.MailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("mail")
@RequiredArgsConstructor
public class EmailController {
    private final MailServiceImpl mailService;

    @PostMapping("/send-email/{id}")
    public void sendEmail(@RequestBody Email emailMessage, @PathVariable("id") int id ) {
        mailService.sendEmail(emailMessage, id );
    }


}
