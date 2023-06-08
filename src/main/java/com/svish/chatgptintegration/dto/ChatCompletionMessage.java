package com.svish.chatgptintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionMessage implements Serializable {

    private String role;

    private String content;

}
