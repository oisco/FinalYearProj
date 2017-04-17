package com.example.UfcAPIConsumer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    @Autowired
    StartupService startupService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //change below to use pattern
      @Scheduled(cron="0 0 21 * * SUN")//9// o clock on Sundays
    public void reportCurrentTime() {
        startupService.onStartup();
        log.info("Algorithm and data updated", dateFormat.format(new Date()));
    }
}