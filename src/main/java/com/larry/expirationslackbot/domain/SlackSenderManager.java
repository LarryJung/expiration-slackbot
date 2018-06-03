package com.larry.expirationslackbot.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larry.expirationslackbot.repository.FoodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SlackSenderManager {

    @Autowired
    private FoodRepository foodRepository;


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public SlackSenderManager(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public boolean send(SlackTargetEnum target, Object object) {
        try {
            restTemplate.postForEntity(target.getWebHookUrl(), writeValueAsString(object), String.class);
            return true;
        } catch (Exception e) {
            log.error("Occur Exception: {}", e);
            return false;
        }
    }

    private String writeValueAsString(Object obj) {
        String json = null;
        try {
            objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
            json = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Occur JsonProcessingException: {}", e);
        }
        return json;
    }

    public void sendWarnings() {
        SlackMessageDto.MessageButtons dto = getMessageButtons();
        send(SlackTargetEnum.CH_BOT, dto);
    }

    private SlackMessageDto.MessageButtons getMessageButtons() {
        List<SlackMessageDto.MessageButtonAttachment> attachments = foodRepository.findAll().stream().filter(Food::isSend)
                .map(Food::toAttachment).collect(Collectors.toList());

        return SlackMessageDto.MessageButtons.builder()
                .text("유통기한이 다가오니 먹던지 버리던지...")
                .attachments(attachments).build();
    }


}
