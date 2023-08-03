package com.ibad.demo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.ibad.demo.controller.UserRegistrationController;
import com.ibad.demo.response.GeolocationResponse;
import com.ibad.demo.response.RegistrationResponse;
import com.ibad.demo.validation.UserRegistrationRequest;

import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserRegistrationControllerTest {

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Test
    public void testRegisterUserSuccess() {
        try {
            // Create a sample UserRegistrationRequest for testing
            UserRegistrationRequest request = new UserRegistrationRequest();
            request.setIpAddress("123.456.789.0");
            request.setPassword("Passwor1#");
            request.setUsername("testuser");

            // Create a sample GeolocationResponse for success scenario
            GeolocationResponse geolocationResponse = new GeolocationResponse();
            geolocationResponse.setCountry("Canada");
            geolocationResponse.setCity("TestCity");

            // Mock the behavior of WebClient
            WebClient webClient = WebClient.create();

            when(webClientBuilder.baseUrl(UserRegistrationController.getGeolocationApiUrl()).build()).thenReturn(webClient);
            when(webClient.get()
                    .uri(uriBuilder -> uriBuilder.build(request.getIpAddress()))
                    .retrieve()
                    .bodyToMono(GeolocationResponse.class)).thenReturn(Mono.just(geolocationResponse));

            // Perform the test
            Mono<ResponseEntity<?>> resultMono = userRegistrationController.registerUser(request);
            
        

            // Get the result from the Mono using .block() and assert the response
            ResponseEntity<?> responseEntity = resultMono.block();
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            RegistrationResponse response = (RegistrationResponse) responseEntity.getBody();
            assertEquals("Welcome, testuser! You are in TestCity, Canada.", response.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    // @Test
    // public void testRegisterUserNotInCanada() {
    //     // Create a sample UserRegistrationRequest for testing
    //     UserRegistrationRequest request = new UserRegistrationRequest();
    //     request.setIpAddress("123.456.789.0");
    //     request.setUsername("testuser");

    //     // Create a sample GeolocationResponse for scenario where the user is not in Canada
    //     GeolocationResponse geolocationResponse = new GeolocationResponse();
    //     geolocationResponse.setCountry("US");
    //     geolocationResponse.setCity("TestCity");

    //     // Mock the behavior of WebClient
    //     WebClient webClient = WebClient.create();
    //     when(webClientBuilder.baseUrl(userRegistrationController.getGeolocationApiUrl()).build()).thenReturn(webClient);
    //     when(webClient.get()
    //             .uri(uriBuilder -> uriBuilder.build(request.getIpAddress()))
    //             .retrieve()
    //             .bodyToMono(GeolocationResponse.class)).thenReturn(Mono.just(geolocationResponse));

    //     // Perform the test
    //     Mono<ResponseEntity<?>> resultMono = userRegistrationController.registerUser(request);
    //     StepVerifier.create(resultMono)
    //             .expectNextMatches(responseEntity -> responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST)
    //             .verifyComplete();
    // }
}
