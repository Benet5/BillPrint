package Billprint.Import.Client;
import Billprint.Import.Item.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test

    void ShouldPostAndGetClients(){
        Address address1 = new Address("Würstchenbude", "Heimathafen 1", "225005 Helgoland");
        List<Link> links = List.of(Link.of("/api/clients", "self"));
        Client client = new Client("12345",address1, true, 3, 2);
        ClientDTO client1 =ClientDTO.of(client);


        ClientDTO client2 = new ClientDTO("Würstchenbude2", "Heimathafen 1", "225005 Helgoland", true, 5, 3, links);
        ClientDTO client3 = new ClientDTO("Würstchenbude3", "Heimathafen 1", "225005 Helgoland", true, 5, 3, links);

        ResponseEntity<ClientDTO> postResponse1 = restTemplate.exchange("/api/clients", HttpMethod.POST, new HttpEntity<>(client1), ClientDTO.class);
        assertEquals(postResponse1.getStatusCode(), HttpStatus.CREATED);


        String client1ID =postResponse1.getBody().getLinks().get(0).getHref();

        ResponseEntity<ClientDTO> putResponse = restTemplate.exchange(client1ID, HttpMethod.PUT, new HttpEntity<>(client3), ClientDTO.class);
        assertEquals(putResponse.getStatusCode(), HttpStatus.OK);


        ResponseEntity<ClientDTO> postResponse2 = restTemplate.exchange("/api/clients", HttpMethod.POST, new HttpEntity<>(client2), ClientDTO.class);
        assertEquals(postResponse2.getStatusCode(), HttpStatus.CREATED);


        ResponseEntity<ClientDTO[]> getResponse = restTemplate.exchange("/api/clients", HttpMethod.GET, new HttpEntity<>(""), ClientDTO[].class);
        assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        assertTrue(getResponse.getBody().length == 2);


        ResponseEntity<Void> DeleteResponse = restTemplate.exchange(client1ID, HttpMethod.DELETE, new HttpEntity<>(""), Void.class);
        assertEquals(DeleteResponse.getStatusCode(), HttpStatus.OK);

        ResponseEntity<ClientDTO[]> getResponse2 = restTemplate.exchange("/api/clients", HttpMethod.GET, new HttpEntity<>(""), ClientDTO[].class);
        assertEquals(getResponse2.getStatusCode(), HttpStatus.OK);
        assertTrue(getResponse2.getBody().length == 1);


    }





}