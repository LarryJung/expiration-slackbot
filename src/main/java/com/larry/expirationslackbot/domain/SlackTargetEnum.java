package com.larry.expirationslackbot.domain;

import lombok.Getter;

@Getter
public enum SlackTargetEnum {

    CH_BOT("https://hooks.slack.com/services/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", "잡담");

    private final String webHookUrl;
    private final String channel;

    SlackTargetEnum(String webHookUrl, String channel) {
        this.webHookUrl = webHookUrl;
        this.channel = channel;
    }
}
