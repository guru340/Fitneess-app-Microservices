package com.fitness.AiServices.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GroqServices {


    private final WebClient webClient;

    @Value("${spring.ai.openai.base-url}")
    private String GroqApiUrl;

    @Value("${spring.ai.openai.api-key}")
    private String GroqApikey;

    public GroqServices(WebClient.Builder webClientBuilder){
        this.webClient=webClientBuilder.build();
    }

    public String getRecommendations(String details) {

        Map<String, Object> requestBody = Map.of(
                "model", "openai/gpt-oss-120b",
                "messages", new Object[]{
                        Map.of(
                                "role", "user",
                                "content", details
                        )
                }
        );

        String response = webClient.post()
                .uri(GroqApiUrl + "/chat/completions")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + GroqApikey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
