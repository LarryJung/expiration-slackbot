package com.larry.expirationslackbot.domain;

import com.larry.expirationslackbot.repository.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Scheduled(cron = "0/10 * * * * ?")
    public void execute() {
        List<Food> warnings = foodRepository.findAll().stream().filter(f -> {
            return (f.isStatus(LocalDateTime.now()) == FoodStatus.WARNING) || (f.isStatus(LocalDateTime.now()) == FoodStatus.EXPIRED);
        }).collect(Collectors.toList());



        List<SlackMessageDto.Action> actions = new ArrayList<>();
        actions.add(SlackMessageDto.Action.builder()
                .type("button")
                .text("이미 먹었다.")
                .url("https://www.naver.com/").build());

        List<SlackMessageDto.MessageButtonAttachment> attachments = new ArrayList<>();
        attachments.add(SlackMessageDto.MessageButtonAttachment.builder()
                .actions(actions).build());

        SlackMessageDto.MessageButtons dto = SlackMessageDto.MessageButtons.builder()
                .text("유통기한이 다가오니 먹던지 버리던지 해라.")
                .attachments(attachments).build();

        slackSenderManager.send(SlackTargetEnum.CH_BOT, dto);
    }
}
