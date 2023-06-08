package com.svish.chatgptintegration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "chatgpt.completions")
@Data
public class CompletionsConfig {

    private Integer maxToken;

    private Double temperature;

    private Double topP;

    private String url;

    private String model;

}
