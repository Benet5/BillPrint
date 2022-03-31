package Billprint.Import.Client;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends MongoRepository<Client, String> {

 Optional<Client> findByAddressName(String name);

}
