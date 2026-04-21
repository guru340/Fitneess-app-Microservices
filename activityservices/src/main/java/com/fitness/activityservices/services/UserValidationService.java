package com.fitness.activityservices.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    private final WebClient userserviceWebclient;

    public boolean valiateUser(String userId) {
        log.info("calling user Service",userId);
        try {
            return Boolean.TRUE.equals(userserviceWebclient.get().uri("/api/users/{userId}/validate", userId)
                    .retrieve().bodyToMono(Boolean.class).block());
        }catch (WebClientException e){
            e.printStackTrace();

        }
        return false;
    }
}
