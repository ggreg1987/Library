package io.github.ggreg1987.Library.domain.rest.service.impl;

import io.github.ggreg1987.Library.domain.entities.Loan;
import io.github.ggreg1987.Library.domain.rest.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {

   private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

   private final LoanService loanService;
   private final EmailService emailService;

   @Scheduled(cron = CRON_LATE_LOANS)
   public void sendEmailToLateLoans() {
       List<Loan> allLateLoans = loanService.getAllLateLoans();
       allLateLoans
               .stream()
               .map(loan -> loan.getCustomerEmail())
               .collect(Collectors.toList());
   }
}
