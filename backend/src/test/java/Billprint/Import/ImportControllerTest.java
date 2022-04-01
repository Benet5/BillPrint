package Billprint.Import;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImportControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    // Reminder: Integrationstest nachholen, sobald Andre den Hinweis zum createItem via Csv gegeben hat.
    @Test
    void ShouldPerformAllRequests(){

        String a = "e";
        String b = "e";

        assertEquals(a,b);

    }

}