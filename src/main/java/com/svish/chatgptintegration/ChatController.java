package com.svish.chatgptintegration;

import com.svish.chatgptintegration.dto.ChatResponse;
import com.svish.chatgptintegration.dto.MessageRequest;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/chatGPT")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;

    @PostMapping("/completions")
    @CrossOrigin
    public ResponseEntity<ChatResponse> sendCompletions(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false)
            @Parameter(hidden = true) String authorization,
            @RequestBody MessageRequest request
    ) {

        MDC.put(HttpHeaders.AUTHORIZATION,
                Optional.ofNullable(authorization).filter(StringUtils::isNotBlank).orElse(null));
        return service.sendCompletions(request);

    }

    @PostMapping("/chatCompletion")
    @CrossOrigin
    public ResponseEntity<ChatResponse> sendChatCompletion(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false)
            @Parameter(hidden = true) String authorization,
            @RequestBody MessageRequest request
    ) {

        MDC.put(HttpHeaders.AUTHORIZATION,
                Optional.ofNullable(authorization).filter(StringUtils::isNotBlank).orElse(null));
        return service.sendChatCompletions(request);

    }

    @DeleteMapping("/chatCompletion")
    @CrossOrigin
    public ResponseEntity<String> resetChat() {

        service.messages.clear();
        return new ResponseEntity<>("Conversation cleared.", HttpStatus.OK);
    }
}
