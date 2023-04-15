package com.svish.chatgptintegration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svish.chatgptintegration.config.ChatGptIntegrationConfig;
import com.svish.chatgptintegration.dto.ChatRequestDto;
import com.svish.chatgptintegration.dto.ChatResponseDto;
import com.svish.chatgptintegration.dto.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final ChatGptIntegrationConfig config;

    public ResponseEntity<ChatResponseDto> send(MessageRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION,
                Optional.of(
                                Optional.ofNullable(
                                                Optional.ofNullable(
                                                        MDC.get(HttpHeaders.AUTHORIZATION)
                                                ).orElse(config.getAccessToken())
                                        ).filter(x -> !(x.isBlank() || x.isEmpty()))
                                        .orElseThrow(
                                                () -> new RuntimeException(
                                                        "API key not provided. "
                                                                + "Please make sure that the API key was sent with the "
                                                                + "request or is added in the application configuration."
                                                )
                                        )
                        )
                        .filter(t -> t.startsWith("Bearer")).orElse("Bearer " + MDC.get(HttpHeaders.AUTHORIZATION))
        );

        ChatRequestDto chatRequest = ChatRequestDto.builder()
                .temperature(config.getTemperature())
                .model(config.getModel())
                .maxTokens(config.getMaxToken())
                .topP(config.getTopP())
                .prompt(request.getUserMessage())
                .build();

        HttpEntity<ChatRequestDto> entity = new HttpEntity<>(chatRequest, headers);

        ChatResponseDto responseDto = new ChatResponseDto();
        HttpStatusCode httpStatus = null;

        try {

            ResponseEntity<String> response = restTemplate.exchange(config.getUrl(), HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                httpStatus = response.getStatusCode();

                throw new Exception("Received unsuccessful response with status code: "
                        + httpStatus);
            }

            responseDto = objectMapper.readValue(response.getBody(), ChatResponseDto.class);
            httpStatus = HttpStatus.OK;

        } catch (Exception e) {

            responseDto.setError("Message: " + e.getMessage() + "\n Details: " + e.getLocalizedMessage());
            httpStatus = Optional.ofNullable(httpStatus).orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<>(responseDto, httpStatus);

    }
}
