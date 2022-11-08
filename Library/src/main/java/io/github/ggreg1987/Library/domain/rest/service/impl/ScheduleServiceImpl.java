package io.github.ggreg1987.Library.domain.rest.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl {

   private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";
}
