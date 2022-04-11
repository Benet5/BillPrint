package Billprint.Mapping;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClientToPrintRepo extends MongoRepository <ClientToPrint, String> {


    Optional<ClientToPrint> findByAddressName(String name);
}
