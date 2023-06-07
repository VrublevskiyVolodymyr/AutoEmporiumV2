package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.models.Advertisement;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String email, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText("<h2>" + body + "</h2>", true);
            helper.setFrom(new InternetAddress("v637904@gmail.com"));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }
}
