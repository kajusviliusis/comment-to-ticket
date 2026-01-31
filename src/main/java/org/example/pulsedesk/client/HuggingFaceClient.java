package org.example.pulsedesk.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HuggingFaceClient {
    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.model}")
    private String model;

    private final WebClient webClient = WebClient.create("https://api-inference.huggingface.co");

    public String analyze(String prompt)
    {
        return webClient.post()
                .uri("/models/" + model)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue("{\"inputs\": \"" + prompt + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
