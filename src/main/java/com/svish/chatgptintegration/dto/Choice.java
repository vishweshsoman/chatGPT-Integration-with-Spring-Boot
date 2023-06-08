package com.svish.chatgptintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Choice implements Serializable {

    private String text;

    private String index;

    @JsonProperty("finish_reason")
    private String finishReason;

    private String logprobs;

    private ChatCompletionMessage message;

}
