package com.svish.chatgptintegration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "chatgpt.chat-completion")
@Data
public class ChatCompletionConfig {

    private String url;

    private String model;

}
