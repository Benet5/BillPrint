package Billprint.Import.Item;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    private String id;


    private String customer;
    private String listingID;
    private Ad ad;
    private String name;
    private Address address;

}
