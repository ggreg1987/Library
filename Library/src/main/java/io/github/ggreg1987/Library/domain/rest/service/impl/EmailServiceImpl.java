package io.github.ggreg1987.Library.domain.rest.service.impl;

import io.github.ggreg1987.Library.domain.rest.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sandMails(String message, List<String> mailList) {

    }
}
