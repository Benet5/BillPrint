package Billprint.Import.Client;
import Billprint.Auth.User.*;
import Billprint.Import.Item.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.Link;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private WhitelistRepo whitelistRepo;

    @Test

    void ShouldPostAndGetClients(){
        //loginuser

        Whitelist whitelistItem = new Whitelist("5555","tester@gott1.de");
        when(whitelistRepo.findByEmail("tester@gott1.de")).thenReturn(Optional.of(whitelistItem));

        ResponseEntity<AppUser> createUserResponse = restTemplate.postForEntity("/auth/create", new RegisterData("tester@gott1.de", "123456", "123456"), AppUser.class);
        assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createUserResponse.getBody().getEmail().equals("tester@gott.de"));

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/auth/login", new LoginData("tester@gott1.de", "123456"), String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotEmpty();



        Address address1 = new Address("Würstchenbude4", "Heimathafen 1", "225005 Helgoland");
        List<Link> links = List.of(Link.of("/api/clients", "self"));
        Client client = new Client("12345",address1, true, 3, 2);
        ClientDTO client1 =ClientDTO.of(client);


        ClientDTO client2 = new ClientDTO("Würstchenbude6", "Heimathafen 1", "225005 Helgoland", true, 5, 3, links);
        ClientDTO client3 = new ClientDTO("Würstchenbude3", "Heimathafen 1", "225005 Helgoland", true, 5, 3, links);

        ResponseEntity<ClientDTO> postResponse1 = restTemplate.exchange("/api/clients", HttpMethod.POST, new HttpEntity<>(client1, createHeaders(loginResponse.getBody(), loginResponse.getHeaders().get("Set-Cookie").get(0))), ClientDTO.class);
        assertEquals(postResponse1.getStatusCode(), HttpStatus.CREATED);


        String client1ID=postResponse1.getBody().getLinks().get(0).getHref();

        ResponseEntity<ClientDTO> putResponse = restTemplate.exchange(client1ID, HttpMethod.PUT, new HttpEntity<>(client3, createHeaders(loginResponse.getBody(), postResponse1.getHeaders().get("Set-Cookie").get(1))), ClientDTO.class);
        assertEquals(putResponse.getStatusCode(), HttpStatus.OK);


        ResponseEntity<ClientDTO> postResponse10 = restTemplate.exchange("/api/clients", HttpMethod.POST, new HttpEntity<>(client2, createHeaders(loginResponse.getBody(), putResponse.getHeaders().get("Set-Cookie").get(1))), ClientDTO.class);
        assertEquals(postResponse10.getStatusCode(), HttpStatus.CREATED);


        ResponseEntity<ClientDTO[]> getResponse = restTemplate.exchange("/api/clients", HttpMethod.GET, new HttpEntity<>("", createHeaders(loginResponse.getBody(), postResponse10.getHeaders().get("Set-Cookie").get(1))), ClientDTO[].class);
        assertEquals(getResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(getResponse.getBody().length, 5); //2


        ResponseEntity<Void> DeleteResponse = restTemplate.exchange(client1ID, HttpMethod.DELETE, new HttpEntity<>("", createHeaders(loginResponse.getBody(), getResponse.getHeaders().get("Set-Cookie").get(1))), Void.class);
        assertEquals(DeleteResponse.getStatusCode(), HttpStatus.OK);

        ResponseEntity<ClientDTO[]> getResponse2 = restTemplate.exchange("/api/clients", HttpMethod.GET, new HttpEntity<>("", createHeaders(loginResponse.getBody(), DeleteResponse.getHeaders().get("Set-Cookie").get(1))), ClientDTO[].class);
        assertEquals(getResponse2.getStatusCode(), HttpStatus.OK);
        assertTrue(getResponse2.getBody().length == 4); //1


    }



    private HttpHeaders createHeaders(String token, String csrf){
        String authHeader = "Bearer " + token;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        String csrfNew = csrf.substring(11,csrf.indexOf(';')).trim();
        headers.set("X-XSRF-TOKEN", csrfNew);
        headers.set("Cookie", "XSRF-TOKEN="+csrf.substring(11));
        return headers;
    }

}