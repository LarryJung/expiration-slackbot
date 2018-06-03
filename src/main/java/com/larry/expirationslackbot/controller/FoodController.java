package com.larry.expirationslackbot.controller;


import com.larry.expirationslackbot.domain.SlackSenderManager;
import com.larry.expirationslackbot.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/foods")
@Controller
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private SlackSenderManager slackSenderManager;

    @GetMapping("/remove/{id}")
    public void remove(@PathVariable Long id) {
        slackSenderManager.sendDeleteConfirmMessage(id);
        foodRepository.deleteById(id);
    }

}
