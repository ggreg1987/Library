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
    private String  message;

    private final JavaMailSender javaMailSender;
    @Override
    public void sandMails(String message, List<String> mailList) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    }
}
