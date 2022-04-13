package Billprint.Auth.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final WhitelistService whitelistService;


    @PostMapping("/create")
    public ResponseEntity<AppUser> createUser(@RequestBody RegisterData loginData) {
        if (appUserService.findByEmail(loginData.getEmail()).isEmpty()) {
            if (loginData.getPasswordValidate().equals(loginData.getPassword()) && whitelistService.check(loginData.getEmail())) {
                loginData.setPassword(passwordEncoder.encode(loginData.getPassword()));
                return ResponseEntity.status(201).body(appUserService.createUser(loginData));
            } else return ResponseEntity.status(404).build();
        } else return ResponseEntity.status(409).build();
    }

    @PostMapping("/whitelist/add")
    public void createWhitelistItem(@RequestBody String email){
        whitelistService.createItem(email);
    }

}
