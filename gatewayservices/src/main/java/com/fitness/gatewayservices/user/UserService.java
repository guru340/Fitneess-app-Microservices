package com.fitness.gatewayservices.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final WebClient userserviceWebclient;

    public Mono<Boolean> valiateUser(String userId) {
        log.info("calling user Service",userId);

            return userserviceWebclient.get().uri("/api/users/{userId}/validate", userId)
                    .retrieve().bodyToMono(Boolean.class).onErrorResume(WebClientResponseException.class, e->{
                        if(e.getStatusCode()== HttpStatus.NOT_FOUND) {
                            return Mono.error(new RuntimeException("User not Found" +userId));
                        }    else if(e.getStatusCode()== HttpStatus.BAD_REQUEST) {
                            return Mono.error(new RuntimeException("Invalid" +userId));
                        }
                        return Mono.error(new RuntimeException("UnExceptedError" +userId));
                    });
    }

    public Mono<UserResponse> registerUser(RegisterRequest registerRequest) {

        log.info("Calling User Registration for {}",registerRequest.getEmail());

        return userserviceWebclient.post().uri("/api/users/registar")
                .bodyValue(registerRequest)
                .retrieve().bodyToMono(UserResponse.class).onErrorResume(WebClientResponseException.class, e->{
                     if(e.getStatusCode()== HttpStatus.BAD_REQUEST) {
                        return Mono.error(new RuntimeException("BAD Request" +e.getMessage()));
                    }
                    return Mono.error(new RuntimeException("UnExceptedError" +e.getMessage()));
                });
    }
}
