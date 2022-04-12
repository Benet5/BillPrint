package Billprint.Auth.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping
    public AppUser createUser(@RequestBody LoginData loginData) throws RuntimeException{
        if(loginData.getPasswordValidate().equals(loginData.getPassword())) {
            loginData.setPassword(passwordEncoder.encode(loginData.getPassword()));
            return appUserService.createUser(loginData);
        } else throw new RuntimeException("Passwörter stimmen nicht überein.");
    }
}
