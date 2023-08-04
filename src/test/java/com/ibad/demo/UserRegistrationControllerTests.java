package com.ibad.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.ibad.demo.controller.UserRegistrationController;

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
}
