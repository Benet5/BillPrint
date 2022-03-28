package Billprint.Import;



import Billprint.Import.Item.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CSVRepo extends MongoRepository <Item, String> {



}
