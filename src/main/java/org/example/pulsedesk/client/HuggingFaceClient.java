package org.example.pulsedesk.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HuggingFaceClient {
    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.model}")
    private String model;

    private final WebClient webClient = WebClient.create("https://router.huggingface.co");

    public String analyze(String prompt)
    {
        log.info("Calling Hugging Face model '{}' with prompt: {}", model, prompt);

        Map<String, Object> payload = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0
        );

        String response = webClient.post()
                .uri("/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("HF raw response: {}", response);
        return response;
    }

}
