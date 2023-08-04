package com.ibad.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.rmi.UnexpectedException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.ibad.demo.controller.UserRegistrationController;
import com.ibad.demo.response.GeolocationResponse;
import com.ibad.demo.response.RegistrationResponse;

@SpringBootTest
public class UserRegistrationControllerTests {
   @Mock
    private WebClient.Builder mockWebClientBuilder;
private UserRegistrationController userRegistrationController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userRegistrationController = new UserRegistrationController(mockWebClientBuilder);
    }

     @Test
    public void testConstructor() {
        // Arrange
        WebClient.Builder expectedWebClientBuilder = mock(WebClient.Builder.class);
        when(mockWebClientBuilder.baseUrl("your_base_url")).thenReturn(expectedWebClientBuilder);

        // Act
        UserRegistrationController controller = new UserRegistrationController(mockWebClientBuilder);

        WebClient.Builder actualWebClientBuilder = controller.getWebClientBuilder();
        // Assert
        try {
            assertEquals(expectedWebClientBuilder, actualWebClientBuilder);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    void testGenerateRandomYuid () {
        UUID mockUuid = UUID.fromString("fcad-sdfds-sfdsf-dgfew-vafa");

        UUID randomUuid = mock(UUID.class);

        when(randomUuid.toString()).thenReturn(mockUuid.toString());

        doReturn(randomUuid).when(UUID.class);

        String yuid = userRegistrationController.generateRandomYuid();
    
        assertFalse(yuid.contains("-"));
    }

    //--------------------------------------------------------------------------------------------------
    //Test Cases For GeolocationResponse Class

    @Test
    public void GeolocationResponseTest() {
        // Create a GeolocationResponse object
        GeolocationResponse response = new GeolocationResponse();

        // Test initial values
        assertNull(response.getCity());
        assertNull(response.getCountry());
        assertNull(response.getRegion());

        // Test setter methods
        response.setCity("New York");
        response.setCountry("United States");
        response.setRegion("North America");

        // Test getter methods
        assertEquals("New York", response.getCity());
        assertEquals("United States", response.getCountry());
        assertEquals("North America", response.getRegion());

        // Test setting values to null
        response.setCity(null);
        response.setCountry(null);
        response.setRegion(null);

        // Test getter methods after setting to null
        assertNull(response.getCity());
        assertNull(response.getCountry());
        assertNull(response.getRegion());
    }

    //--------------------------------------------------------------------------------------------------
    //Test Cases For RegistrationResponse Class

    @Test
    public void RegistrationResponseTest() throws UnexpectedException {
        // Create a RegistrationResponse object
        RegistrationResponse response = new RegistrationResponse("12345", "Welcome to our platform!");

        // Test getter methods
        assertEquals("12345", response.getYuid());
        assertEquals("Welcome to our platform!", response.getMessage());

        // Test setter methods
        response.setYuid("67890");
        response.setMessage("You have successfully registered!");

        // Test getter methods after setting new values
        assertEquals("67890", response.getYuid());
        assertEquals("You have successfully registered!", response.getMessage());
    }
}
