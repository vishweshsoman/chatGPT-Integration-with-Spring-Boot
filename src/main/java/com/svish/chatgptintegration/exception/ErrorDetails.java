package com.svish.chatgptintegration.exception;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ErrorDetails implements Serializable {

    private String errorMessage;

    private Integer statusCode;

}
