package com.svish.chatgptintegration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "chatgpt")
@Data
public class ChatGptIntegrationConfig {

    private String accessToken;

    private Integer maxToken;

    private Double temperature;

    private Double topP;

    private String url;

    private String model;

}
