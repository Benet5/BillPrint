package Billprint.Auth.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepo appUserRepo;

    public UserDTO createUser(RegisterData loginData) throws RuntimeException{
        if(appUserRepo.findByEmail(loginData.getEmail()).isEmpty()) {
            AppUser user = new AppUser();
            user.setPassword(loginData.getPassword());
            user.setEmail(loginData.getEmail());
            AppUser saved= appUserRepo.save(user);
            return UserDTO.of(saved.getEmail());
        } else {
            throw new RuntimeException("Fehler bei der Accountanlage");
        }
    }

    public Optional<AppUser> findByEmail(String email){
        return appUserRepo.findByEmail(email);
    }

    public List<UserDTO> findAll(){
       return appUserRepo.findAll()
                 .stream().map(e -> UserDTO.of(e.getEmail())).toList();
    }
}
