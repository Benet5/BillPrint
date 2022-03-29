package Billprint.Import.Item;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {


    private String customer;
    private String listingID;
    private AdDTO ad;
    private String name;
    private Address address;
    private List<Link> links;


    public static ItemDTO of(Item item) {

        List<Link> links = List.of(
                Link.of("api/items/" + item.getId(), "self")
        );
        return new ItemDTO(
                item.getCustomer(),
                item.getListingID(),
                AdDTO.of(item.getAd()),
                item.getName(),
                item.getAddress(),
                links

        );
    }


    public Item toItem(){
        Ad ad =new Ad(this.getAd().getTitle(),this.getAd().getType(),this.getAd().getRuntime(), this.getAd().getListingAction(),this.getAd().toDate(getAd().getDate()),this.getAd().getJobLocation());
        Address address = new Address(this.getAddress().getName(), this.getAddress().getStreet(), this.getAddress().getLocation());
        Item item2 = new Item(null, customer, listingID, ad, name, address);
        return item2;
    }
}
