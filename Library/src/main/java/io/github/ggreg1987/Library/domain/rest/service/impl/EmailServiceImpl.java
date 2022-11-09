package io.github.ggreg1987.Library.domain.rest.service.impl;

import io.github.ggreg1987.Library.domain.rest.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${application.mail.default-remitter}")
    private String remitter;

    private final JavaMailSender javaMailSender;
    @Override
    public void sendMails(String message, List<String> mailList) {

        String[] listMail = mailList.toArray(new String[mailList.size()]);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(remitter);
        simpleMailMessage.setSubject("Late Book Loan");
        simpleMailMessage.setText(message);
        simpleMailMessage.setTo(listMail);

        javaMailSender.send(simpleMailMessage);
    }
}
