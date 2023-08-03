package com.ibad.demo;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;
@RestController
@Validated
public class UserRegistrationController {


    private static final String GEOLOCATION_API_URL = "http://ip-api.com/json/{ip}";
    
    private final WebClient.Builder webClientBuilder;
    public UserRegistrationController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    @PostMapping("/register")
    public Mono<ResponseEntity<?>> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        WebClient webClient = webClientBuilder.baseUrl(GEOLOCATION_API_URL).build();
         
        Mono<GeolocationResponse> geolocationMono = webClient.get()
                                                    .uri(uriBuilder -> uriBuilder.build(request.getIpAddress()))
                                                    .retrieve()
                                                    .bodyToMono(GeolocationResponse.class);
        return geolocationMono.flatMap(geolocation -> {
                    if (!"Canada".equalsIgnoreCase(geolocation.getCountry())) {
                        return Mono.just(ResponseEntity.badRequest().body("User is not eligible to register. IP address is not located in Canada."));
                    }
                    String yuid = generateRandomYuid();
                    String welcomeMessage = "Welcome, " + request.getUsername() + "! You are in " + geolocation.getCity() + ", " + geolocation.getCountry() + ".";
                    RegistrationResponse response = new RegistrationResponse(yuid, welcomeMessage);
                    return Mono.just(ResponseEntity.ok(response));
                })
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage())));
        
    }

    private String generateRandomYuid() {
        UUID uuid = UUID.randomUUID();
        String yuid = uuid.toString().replaceAll("-", "");
        return yuid;
    }
}