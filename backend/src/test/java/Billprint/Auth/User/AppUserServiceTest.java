package Billprint.Auth.User;


import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static Billprint.Auth.User.UserRole.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserServiceTest {
    @Test

    void SHouldCreateAnUser(){
        AppUserRepo appUserRepo = mock(AppUserRepo.class);
        AppUserService appUserService = new AppUserService(appUserRepo);

        RegisterData register = new RegisterData("bieneMaja@web.de", "musmus", "musmus");
        AppUser user = new AppUser();
        user.setEmail("bieneMaja@web.de");
        user.setPassword("musmus");
        AppUser user2 = new AppUser("58749","bieneMaja@web.de", "musmus", USER );

        when(appUserRepo.findByEmail("bieneMaja@web.de")).thenReturn(Optional.empty());
        when(appUserRepo.save(user)).thenReturn(user2);

        var actual = appUserService.createUser(register);

        verify(appUserRepo).save(user);
        assertEquals(actual.getEmail(), "bieneMaja@web.de");


    }

    @Test
    void ShouldFindAndDeleteUsers(){
        AppUserRepo appUserRepo = mock(AppUserRepo.class);
        AppUserService appUserService = new AppUserService(appUserRepo);
        AppUser user1 = new AppUser("58749","bieneMaja@web.de", "musmus", USER );
        AppUser user2 = new AppUser("58w39","DrohneWilli@web.de", "sumsum", USER );

        when(appUserRepo.findAll()).thenReturn(List.of(user1, user2));
        var actual = appUserService.findAll();

        assertTrue(actual.size() ==2);
        assertEquals(actual.get(0).getEmail(), "bieneMaja@web.de");

        appUserService.deleteByEmail("bieneMaja@web.de");
        verify(appUserRepo).deleteByEmail("bieneMaja@web.de");

    }


}