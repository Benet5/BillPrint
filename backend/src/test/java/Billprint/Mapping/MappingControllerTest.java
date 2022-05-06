package Billprint.Mapping;

import Billprint.Auth.User.*;
import Billprint.Import.Client.Client;
import Billprint.Import.Client.ClientDTO;
import Billprint.Import.Item.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MappingControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private WhitelistRepo whitelistRepo;

    @Test

    void ShouldMappAllClients(){
        Whitelist whitelistItem = new Whitelist("5555","tester@gott2.de");
        when(whitelistRepo.findByEmail("tester@gott2.de")).thenReturn(Optional.of(whitelistItem));

        ResponseEntity<AppUser> createUserResponse = restTemplate.postForEntity("/auth/create", new RegisterData("tester@gott2.de", "123456", "123456"), AppUser.class);
        assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createUserResponse.getBody().getEmail().equals("tester@gott.de"));

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/auth/login", new LoginData("tester@gott2.de", "123456"), String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotEmpty();



        // Items anlegen
        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Hamburg");
        Ad ad = adDTO.toAd();
        Address address1 = new Address("Würstchenbude2", "Heimathafen 1", "225005 Helgoland");
        Address address2 = new Address("Döner", "Heimathafen 4", "225005 Helgoland");
        Item newItem1 = new Item("12344", "Donalds Polierservice", "bla", ad, "Würstchenbude2", address1);
        Item newItem2 = new Item("12344", "Donalds Polierservice", "bla", ad, "Würstchenbude2", address1);
        ItemDTO item1 = ItemDTO.of(newItem1);
        ItemDTO item2 = ItemDTO.of(newItem2);


        ResponseEntity<ItemDTO> postResponse1 = restTemplate.exchange("/api/import", HttpMethod.POST, new HttpEntity<>(item1, createHeaders(loginResponse.getBody(), loginResponse.getHeaders().get("Set-Cookie").get(0))), ItemDTO.class);
        assertEquals(postResponse1.getStatusCode(), HttpStatus.CREATED);
        ResponseEntity<ItemDTO> postResponse2 = restTemplate.exchange("/api/import", HttpMethod.POST, new HttpEntity<>(item2, createHeaders(loginResponse.getBody(), postResponse1.getHeaders().get("Set-Cookie").get(1))), ItemDTO.class);
        assertEquals(postResponse2.getStatusCode(), HttpStatus.CREATED);


        //Clients anlegen
        Client clientDB1 = new Client("1234567",address1, true, 3, 2);
        Client clientDB2 = new Client("123489", address2, false, 0, 1);
        ClientDTO clientDTO1 =  ClientDTO.of(clientDB1);
        ClientDTO clientDTO2 =  ClientDTO.of(clientDB2);

        ResponseEntity<ClientDTO> postResponse6 = restTemplate.exchange("/api/clients", HttpMethod.POST, new HttpEntity<>(clientDTO1, createHeaders(loginResponse.getBody(), postResponse2.getHeaders().get("Set-Cookie").get(1))), ClientDTO.class);
        assertEquals(postResponse6.getStatusCode(), HttpStatus.CREATED);
        ResponseEntity<ClientDTO> postResponse7 = restTemplate.exchange("/api/clients", HttpMethod.POST, new HttpEntity<>(clientDTO2, createHeaders(loginResponse.getBody(), postResponse6.getHeaders().get("Set-Cookie").get(1))), ClientDTO.class);
        assertEquals(postResponse7.getStatusCode(), HttpStatus.CREATED);


        //Clients mappen

        ResponseEntity<String> putResponse5 = restTemplate.exchange("/api/mapping", HttpMethod.PUT, new HttpEntity<>("", createHeaders(loginResponse.getBody(), postResponse7.getHeaders().get("Set-Cookie").get(1))), String.class);
        assertEquals(putResponse5.getStatusCode(), HttpStatus.OK);
        assertEquals(putResponse5.getBody(), "Es wurden 4 Adressen angepasst und 0 Mandaten angelegt.");


        // ClientToPrints erstellen

        ResponseEntity<String> putResponse6 = restTemplate.exchange("/api/mapping/convert", HttpMethod.PUT, new HttpEntity<>("", createHeaders(loginResponse.getBody(), putResponse5.getHeaders().get("Set-Cookie").get(1))), String.class);
        assertEquals(putResponse6.getStatusCode(), HttpStatus.OK);
        assertEquals(putResponse6.getBody(), "Rechnungs-Daten wurden erfolgreich erstellt.");

    }

    @Test

    void ShouldMappSelected(){

        Whitelist whitelistItem = new Whitelist("5555","tester@gott.de");
        when(whitelistRepo.findByEmail("tester@gott.de")).thenReturn(Optional.of(whitelistItem));

        ResponseEntity<AppUser> createUserResponse = restTemplate.postForEntity("/auth/create", new RegisterData("tester@gott.de", "123456", "123456"), AppUser.class);
        assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createUserResponse.getBody().getEmail().equals("tester@gott.de"));

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/auth/login", new LoginData("tester@gott.de", "123456"), String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotEmpty();

        //Items anlegen
        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Entenhausen");
        Ad ad = adDTO.toAd();
        Address address1 = new Address("Würstchenbude", "Heimathafen 1", "225005 Helgoland");

        Item newItem1 = new Item("12344", "Donalds Polierservice", "bla", ad, "Würstchenbude", address1);
        Item newItem2 = new Item("12344", "Donalds Polierservice", "bla", ad, "Würstchenbude", address1);
        ItemDTO item1 = ItemDTO.of(newItem1);
        ItemDTO item2 = ItemDTO.of(newItem2);

        ResponseEntity<ItemDTO> postResponse1 = restTemplate.exchange("/api/import", HttpMethod.POST, new HttpEntity<>(item1, createHeaders(loginResponse.getBody(), loginResponse.getHeaders().get("Set-Cookie").get(0))), ItemDTO.class);
        assertEquals(postResponse1.getStatusCode(), HttpStatus.CREATED);
        ResponseEntity<ItemDTO> postResponse2 = restTemplate.exchange("/api/import", HttpMethod.POST, new HttpEntity<>(item2, createHeaders(loginResponse.getBody(), postResponse1.getHeaders().get("Set-Cookie").get(1))), ItemDTO.class);
        assertEquals(postResponse2.getStatusCode(), HttpStatus.CREATED);


        //Client anlegen
        Client clientDB1 = new Client("12345",address1, true, 3, 2);
        ClientDTO clientDTO1 =  ClientDTO.of(clientDB1);

        ResponseEntity<ClientDTO> postResponse3 = restTemplate.exchange("/api/clients", HttpMethod.POST, new HttpEntity<>(clientDTO1, createHeaders(loginResponse.getBody(), postResponse2.getHeaders().get("Set-Cookie").get(1))), ClientDTO.class);
        assertEquals(postResponse3.getStatusCode(), HttpStatus.CREATED);


        String id = postResponse1.getBody().getLinks().get(0).getHref();

        //mappen
        ResponseEntity<String> putResponse1 = restTemplate.exchange(id, HttpMethod.PUT, new HttpEntity<>("", createHeaders(loginResponse.getBody(), postResponse3.getHeaders().get("Set-Cookie").get(1))), String.class);
        assertEquals(putResponse1.getStatusCode(), HttpStatus.OK);
        assertEquals(putResponse1.getBody(),"Die Adresse wurde übernommen bzw. neuer Mandant wurde angelegt");

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