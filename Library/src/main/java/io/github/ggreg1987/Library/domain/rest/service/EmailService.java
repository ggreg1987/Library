package io.github.ggreg1987.Library.domain.rest.service;

import java.util.List;

public interface EmailService {
    void sandMails(String message, List<String> mailList);
}
