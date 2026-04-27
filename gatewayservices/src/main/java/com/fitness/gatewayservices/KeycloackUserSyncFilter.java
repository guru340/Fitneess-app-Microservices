package com.fitness.gatewayservices;

import com.fitness.gatewayservices.user.RegisterRequest;
import com.fitness.gatewayservices.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.text.ParseException;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloackUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    // there Should some Value in future
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String  userId=exchange.getRequest().getHeaders().getFirst("X-USER-ID");
        String token=exchange.getRequest().getHeaders().getFirst("Authorization");
        RegisterRequest registerRequest=getUserDetails(token);
        if(userId==null){
            userId= registerRequest.getKeyclockId();
        }

        if(userId!=null && token!=null){
            String finalUserId = userId;
            return userService.valiateUser(userId)
                    .flatMap(exist->{
                        if (!exist) {
                            if (registerRequest != null) {
                                return userService.registerUser(registerRequest)
                                        .then(Mono.empty());
                            } else {
                                return Mono.empty();
                            }
                        }  else{
                            log.info("User already exist,Skipping sync");
                            return Mono.empty();
                        }


                    }
                    )
                    .then(Mono.defer(()->{
                        ServerHttpRequest mutatedrequest=exchange.getRequest().mutate()
                                .header("X-USER-ID", finalUserId)
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedrequest).build());
            }));
        }
        return chain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {
        try {
            String tokenwithoutBrearer=token.replace("Bearer","").trim();
            SignedJWT signedJWT=SignedJWT.parse(tokenwithoutBrearer);
            JWTClaimsSet claimsSet=signedJWT.getJWTClaimsSet();

            RegisterRequest request=new RegisterRequest();
            request.setEmail(claimsSet.getStringClaim("email"));
            request.setKeyclockId(claimsSet.getStringClaim("sub"));
            request.setFirstname(claimsSet.getStringClaim("given_name"));
            request.setLastname(claimsSet.getStringClaim("family_name"));
            request.setPassword("dummy@123");
            return request;
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }
}
