package com.svish.chatgptintegration.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ChatCompletionRequest implements Serializable {

    private String model;

    private List<ChatCompletionMessage> messages;

}
