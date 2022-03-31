package Billprint.Import.Client;

import Billprint.Import.Item.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Clients")
public class Client {

    @Id
    private String id;
    private Address address;
    private boolean tax;
    private int fee;
    private int skonto;

}
