package io.github.ggreg1987.Library.domain.rest.service.impl;

import io.github.ggreg1987.Library.domain.rest.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    @Override
    public void sandMails(String message, List<String> mailList) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        
    }
}
