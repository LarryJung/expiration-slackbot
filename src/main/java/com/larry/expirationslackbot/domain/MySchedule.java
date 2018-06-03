package com.larry.expirationslackbot.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larry.expirationslackbot.repository.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MySchedule {

    private final Logger log = LoggerFactory.getLogger(MySchedule.class);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private SlackSenderManager slackSenderManager;

    @Autowired
    private FoodRepository foodRepository;

    @Scheduled(cron="0/10 * * * * ?")
    public void execute() {
        List<Food> warnings = foodRepository.findAll().stream().filter(f -> {
            return (f.isStatus(LocalDateTime.now()) == FoodStatus.WARNING) || (f.isStatus(LocalDateTime.now()) == FoodStatus.EXPIRED);
        }).collect(Collectors.toList());

        slackSenderManager.send(SlackTargetEnum.CH_BOT, SlackMessageDto.Basic.builder().text("hello world").build());
    }
}
