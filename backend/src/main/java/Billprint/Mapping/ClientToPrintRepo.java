package Billprint.Mapping;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ClientToPrintRepo extends MongoRepository <ClientToPrint, String> {



}
