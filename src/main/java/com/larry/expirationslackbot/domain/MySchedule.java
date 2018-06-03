package com.larry.expirationslackbot.domain;

import com.larry.expirationslackbot.repository.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MySchedule {

    private final Logger log = LoggerFactory.getLogger(MySchedule.class);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private SlackSenderManager slackSenderManager;

    @Scheduled(cron = "0/10 * * * * ?")
    public void execute() {
        slackSenderManager.sendWarnings();
    }
}
