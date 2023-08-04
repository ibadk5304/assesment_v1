package com.ibad.demo.controller;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.ibad.demo.response.GeolocationResponse;
import com.ibad.demo.response.RegistrationResponse;
import com.ibad.demo.validation.UserRegistrationRequest;

import jakarta.validation.Valid;
@RestController
@Validated
public class UserRegistrationController {

    // geolocation api that is fixed
    public static final String GEOLOCATION_API_URL = "http://ip-api.com/json/{ip}";

    private final WebClient.Builder webClientBuilder;

    public UserRegistrationController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        /*
         * Retrieved the location data of that specfic ipAddress
         * response converted into an object format using Mono and then used the flatMap to inspect that object properties
         * Based on different Condition perform the response
         * If user outside Canada: Stop there
         * else if in Canada: 
         *  - generate random id
         *  - and welcome message with geolocation response data
         */
        WebClient webClient = webClientBuilder.baseUrl(GEOLOCATION_API_URL).build();
         
        // this code return an instance of geolocation  
        GeolocationResponse geolocation = webClient.get()
                                                    .uri(uriBuilder -> uriBuilder.build(request.getIpAddress()))
                                                    .retrieve()
                                                    .bodyToMono(GeolocationResponse.class)
                                                    .block();
         
        if (!"Canada".equalsIgnoreCase(geolocation.getCountry())) {
            return ResponseEntity.badRequest().body("User is not eligible to register. IP address is not located in Canada.");
        }
        String yuid = generateRandomYuid();
        String welcomeMessage = "Welcome, " + request.getUsername() + "! You are in " + geolocation.getCity() + ", " + geolocation.getCountry() + ".";
        RegistrationResponse response = new RegistrationResponse(yuid, welcomeMessage);
        return ResponseEntity.ok(response);
        
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) // the exception will handle and display message for any unvalid properties which implemented in UserRegistrationRequest
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = "Validation error: ";
        errorMessage += ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(errorMessage);
    }

    // the will generate a random in a string format and remove any hipen
    public String generateRandomYuid() {
        UUID uuid = UUID.randomUUID();
        String yuid = uuid.toString().replaceAll("-", "");
        return yuid;
    }


    // Getter and setters
    public static String getGeolocationApiUrl() {
        return GEOLOCATION_API_URL;
    }

        public WebClient.Builder getWebClientBuilder() {
        return webClientBuilder;
    }
}