package com.svish.chatgptintegration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svish.chatgptintegration.config.ChatCompletionConfig;
import com.svish.chatgptintegration.config.CompletionsConfig;
import com.svish.chatgptintegration.dto.ChatCompletionMessage;
import com.svish.chatgptintegration.dto.ChatCompletionRequest;
import com.svish.chatgptintegration.dto.CompletionsRequest;
import com.svish.chatgptintegration.dto.ChatResponse;
import com.svish.chatgptintegration.dto.MessageRequest;
import com.svish.chatgptintegration.exception.MissingAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final CompletionsConfig completionsConfig;

    private final ChatCompletionConfig chatCompletionConfig;

    List<ChatCompletionMessage> messages = new ArrayList<>();

    @Value("${chatgpt.accessToken}")
    String apiKey;

    public ResponseEntity<ChatResponse> sendCompletions(MessageRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION,
                Optional.of(
                                Optional.ofNullable(
                                                Optional.ofNullable(
                                                        MDC.get(HttpHeaders.AUTHORIZATION)
                                                ).orElse(apiKey)
                                        ).filter(StringUtils::isNotBlank)
                                        .orElseThrow(
                                                () -> new MissingAuthorizationException(
                                                        "API key not provided. "
                                                                + "Please make sure that the API key was sent with the "
                                                                + "request or is added in the application configuration.",
                                                        HttpStatus.UNAUTHORIZED
                                                )
                                        )
                        )
                        .filter(t -> t.startsWith("Bearer")).orElse("Bearer " + MDC.get(HttpHeaders.AUTHORIZATION))
        );

        CompletionsRequest chatRequest = CompletionsRequest.builder()
                .temperature(completionsConfig.getTemperature())
                .model(completionsConfig.getModel())
                .maxTokens(completionsConfig.getMaxToken())
                .topP(completionsConfig.getTopP())
                .prompt(request.getUserMessage())
                .build();

        HttpEntity<CompletionsRequest> entity = new HttpEntity<>(chatRequest, headers);

        ChatResponse responseDto = new ChatResponse();
        HttpStatusCode httpStatus = null;

        try {

            ResponseEntity<String> response = restTemplate.exchange(completionsConfig.getUrl(), HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                httpStatus = response.getStatusCode();

                throw new Exception("Received unsuccessful response with status code: "
                        + httpStatus);
            }

            responseDto = objectMapper.readValue(response.getBody(), ChatResponse.class);
            httpStatus = HttpStatus.OK;

        } catch (Exception e) {

            responseDto.setError("Message: " + e.getMessage() + "\n Details: " + e.getLocalizedMessage());
            httpStatus = Optional.ofNullable(httpStatus).orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<>(responseDto, httpStatus);

    }

    public ResponseEntity<ChatResponse> sendChatCompletions(MessageRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION,
                Optional.of(
                                Optional.ofNullable(
                                                Optional.ofNullable(
                                                        MDC.get(HttpHeaders.AUTHORIZATION)
                                                ).orElse(apiKey)
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

        this.messages.add(
                new ChatCompletionMessage("user", request.getUserMessage())
        );

        ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
                .model(chatCompletionConfig.getModel())
                .messages(this.messages)
                .build();

        HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(chatRequest, headers);

        ChatResponse responseDto = new ChatResponse();
        HttpStatusCode httpStatus = null;

        try {

            ResponseEntity<String> response = restTemplate.exchange(chatCompletionConfig.getUrl(), HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                httpStatus = response.getStatusCode();

                throw new Exception("Received unsuccessful response with status code: "
                        + httpStatus);
            }

            responseDto = objectMapper.readValue(response.getBody(), ChatResponse.class);
            this.messages.add(responseDto.getChoices().get(0).getMessage());
            httpStatus = HttpStatus.OK;

        } catch (Exception e) {

            responseDto.setError("Message: " + e.getMessage() + "\n Details: " + e.getLocalizedMessage());
            httpStatus = Optional.ofNullable(httpStatus).orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<>(responseDto, httpStatus);

    }
}
