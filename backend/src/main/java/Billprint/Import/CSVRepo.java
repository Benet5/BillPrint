package Billprint.Import;
import Billprint.Import.Item.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CSVRepo extends MongoRepository <Item, String> {

    List<Item> findAllByName(String name);

}
