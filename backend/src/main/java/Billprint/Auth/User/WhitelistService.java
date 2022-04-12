package Billprint.Auth.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WhitelistService {
    private final WhitelistRepo whitelistRepo;

    public void createItem(String email){
        Whitelist whitelist = new Whitelist();
        whitelist.setEmail(email);
        whitelistRepo.save(whitelist);
    }

    public boolean check(String email){
        Optional<Whitelist> actual = whitelistRepo.findByEmail(email);
        return actual.isPresent();
    }

}
