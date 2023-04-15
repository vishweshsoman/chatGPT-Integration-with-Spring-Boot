package com.svish.chatgptintegration;

import com.svish.chatgptintegration.dto.ChatResponseDto;
import com.svish.chatgptintegration.dto.MessageRequestDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;

    @PostMapping("/send")
    @CrossOrigin
    public ResponseEntity<ChatResponseDto> send(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false)
            @Parameter(hidden = true) String authorization,
            @RequestBody MessageRequestDto request
            ) {

        MDC.put(HttpHeaders.AUTHORIZATION,
                Optional.of(authorization).filter(x -> !(x.isEmpty() || x.isBlank())).orElse(null));
        return service.send(request);

    }
}
