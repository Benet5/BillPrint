package Billprint.Auth.User;

import Billprint.Import.Item.ItemDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppUserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private WhitelistRepo whitelistRepo;

   @Test
    void ShouldCreateWhitelistAndUSer(){
        //Nutzeranlage IntialNutzer
        Whitelist whitelistItem = new Whitelist("5555","tester@pabst.de");
       Whitelist whitelistItem2 = new Whitelist("sdfawe","hoppel");
       Whitelist whitelistItem3 = new Whitelist("sdfawasde","happel");
        when(whitelistRepo.findByEmail("tester@pabst.de")).thenReturn(Optional.of(whitelistItem));


        ResponseEntity<AppUser> createUserResponse9 = restTemplate.postForEntity("/auth/create", new RegisterData("tester@pabst.de", "123456", "123456"), AppUser.class);
        assertThat(createUserResponse9.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertEquals(createUserResponse9.getBody().getEmail(), ("tester@pabst.de"));

        ResponseEntity<String> loginResponse9 = restTemplate.postForEntity("/auth/login", new LoginData("tester@pabst.de", "123456"), String.class);
        assertThat(loginResponse9.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse9.getBody()).isNotEmpty();

        //Freischaltung Whitelist
        /*RegisterData register = new RegisterData("bieneMaja@web.de", "musmus", "musmus");
        Principal p = Mockito.mock(Principal.class);
        Mockito.when(p.getName()).thenReturn("bieneMaja@web.de");

         */

        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setEmail("hoppel");

       UserDTO newUserDTO2 = new UserDTO();
       newUserDTO2.setEmail("happel");



        ResponseEntity<UserDTO> postResponse12 = restTemplate.exchange("/auth/whitelist/add", HttpMethod.POST, new HttpEntity<>(newUserDTO, createHeaders(loginResponse9.getBody(), loginResponse9.getHeaders().get("Set-Cookie").get(0))), UserDTO.class);
        assertEquals(postResponse12.getStatusCode(), HttpStatus.CREATED);
        var csrf = postResponse12.getHeaders().get("Set-Cookie").get(1);
       ResponseEntity<UserDTO> postResponse13 = restTemplate.exchange("/auth/whitelist/add", HttpMethod.POST, new HttpEntity<>(newUserDTO2, createHeaders(loginResponse9.getBody(), postResponse12.getHeaders().get("Set-Cookie").get(1))), UserDTO.class);
       assertEquals(postResponse13.getStatusCode(), HttpStatus.CREATED);

       when(whitelistRepo.findByEmail("hoppel")).thenReturn(Optional.of(whitelistItem2));
       when(whitelistRepo.findAll()).thenReturn(List.of(whitelistItem,whitelistItem2,whitelistItem3));
       ResponseEntity<AppUser> createUserResponse10 = restTemplate.postForEntity("/auth/create", new RegisterData("hoppel", "123456", "123456"), AppUser.class);
       assertThat(createUserResponse10.getStatusCode()).isEqualTo(HttpStatus.CREATED);
       assertThat(createUserResponse10.getBody().getEmail().equals("hoppel"));

       when(whitelistRepo.findByEmail("happel")).thenReturn(Optional.of(whitelistItem3));
       ResponseEntity <UserDTO[]> postResponse14 = restTemplate.exchange("/auth/whitelist", HttpMethod.GET, new HttpEntity<>(createHeaders(loginResponse9.getBody(),createUserResponse10.getHeaders().get("Set-Cookie").get(0))), UserDTO[].class);
       assertEquals(postResponse14.getStatusCode(), HttpStatus.OK);
       assertEquals(postResponse14.getBody().length, 3); //3

       postResponse14.getHeaders().get("Set-Cookie").get(0);
       ResponseEntity <Void> deleteResponse1 = restTemplate.exchange("/auth/whitelist?email=happel", HttpMethod.DELETE, new HttpEntity<>(createHeaders(loginResponse9.getBody(), postResponse14.getHeaders().get("Set-Cookie").get(1))), Void.class);
       assertEquals(deleteResponse1.getStatusCode(), HttpStatus.OK);

       ResponseEntity <Void> deleteResponse2 = restTemplate.exchange("/auth?email=hoppel", HttpMethod.DELETE, new HttpEntity<>(createHeaders(loginResponse9.getBody(), deleteResponse1.getHeaders().get("Set-Cookie").get(1))), Void.class);
       assertEquals(deleteResponse2.getStatusCode(), HttpStatus.OK);


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