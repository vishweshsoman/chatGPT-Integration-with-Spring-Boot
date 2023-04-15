package com.svish.chatgptintegration.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ChatResponseDto implements Serializable {

    private String id;

    private String object;

    private Timestamp created;

    private String model;

    private List<Choice> choices;

    private String error;

    private Usage usage;

}
