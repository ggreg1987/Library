package io.github.ggreg1987.Library.domain.rest.service.impl;

import io.github.ggreg1987.Library.domain.rest.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {

   private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

   private final LoanService loanService;
   private final EmailService emailService;
}
