package com.svish.chatgptintegration;

import com.svish.chatgptintegration.dto.ChatResponse;
import com.svish.chatgptintegration.dto.MessageRequest;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                Optional.ofNullable(authorization).filter(x -> !(x.isEmpty() || x.isBlank())).orElse(null));
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
                Optional.ofNullable(authorization).filter(x -> !(x.isEmpty() || x.isBlank())).orElse(null));
        return service.sendChatCompletions(request);

    }
}
