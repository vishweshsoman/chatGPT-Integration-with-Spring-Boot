package com.svish.chatgptintegration.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MessageRequest implements Serializable {

    private String userMessage;

}
