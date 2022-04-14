package Billprint.Auth.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WhitelistRepo extends MongoRepository<Whitelist, String> {
Optional<Whitelist> findByEmail(String email);

void deleteByEmail(String email);
}
