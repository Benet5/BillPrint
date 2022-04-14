package Billprint.Auth.User;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final WhitelistService whitelistService;


    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody RegisterData loginData) {
        if (appUserService.findByEmail(loginData.getEmail()).isEmpty()) {
            if (loginData.getPasswordValidate().equals(loginData.getPassword()) && whitelistService.check(loginData.getEmail())) {
                loginData.setPassword(passwordEncoder.encode(loginData.getPassword()));
                return ResponseEntity.status(201).body(appUserService.createUser(loginData));
            } else return ResponseEntity.status(404).build();
        } else return ResponseEntity.status(409).build();
    }

    @PostMapping("/whitelist/add")
    public ResponseEntity<UserDTO> createWhitelistItem(@RequestBody UserDTO create, Principal principal){
        if(appUserService.findByEmail(principal.getName()).isPresent() && !whitelistService.check(create.getEmail())) {
            whitelistService.createItem(create.getEmail());
            return ResponseEntity.status(201).build();
       } else if (appUserService.findByEmail(principal.getName()).isEmpty())
       { return ResponseEntity.status(403).build();
        }else return ResponseEntity.status(409).build();
    }

    @GetMapping("/whitelist")
    public  ResponseEntity<List<UserDTO>> findAllWhiteListet(Principal principal){
        if(appUserService.findByEmail(principal.getName()).isPresent()){
            return ResponseEntity.status(200)
                    .body(whitelistService.findAll());
        } else return ResponseEntity.status(403).build();
    }


    @DeleteMapping ("/whitelist")
    public ResponseEntity<Void> deleteWhitelist(@RequestParam("email") String email,Principal principal){
        if(appUserService.findByEmail(principal.getName()).isPresent() && whitelistService.check(email)){
            whitelistService.deleteByEmail(email);
            return ResponseEntity.status(200).build();
        } return ResponseEntity.status(400).build();

    }

    @DeleteMapping ()
    public ResponseEntity<Void> deleteUser(@RequestParam("email") String email,Principal principal){
        if(appUserService.findByEmail(principal.getName()).isPresent() && appUserService.findByEmail(email).isPresent()){
            appUserService.deleteByEmail(email);
            return ResponseEntity.status(200).build();
        } return ResponseEntity.status(400).build();

    }

    @GetMapping()
    public  ResponseEntity<List<UserDTO>> findAllAppUsers(Principal principal){
        if(appUserService.findByEmail(principal.getName()).isPresent()){
            return ResponseEntity.status(200)
                    .body(appUserService.findAll());
        } else return ResponseEntity.status(400).build();
    }

}
