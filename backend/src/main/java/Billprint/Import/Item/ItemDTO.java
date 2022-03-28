package Billprint.Import.Item;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {


    private String customer;
    private String listingID;
    private Ad ad;
    private String name;
    private Address address;
    private List<Link> links;





    public static ItemDTO of(Item item) {

        List<Link> links = List.of(
                Link.of("api/items/" + item.getId(), "self")
        );
        return new ItemDTO(
                item.getName(),
                item.getListingID(),
                item.getAd(),
                item.getCustomer(),
                item.getAddress(),
                links

        );
    }


    public static Date toDate(String date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);
            Date newDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            return newDate;
        }


    public Item toItem(){
        Ad ad =new Ad(this.getAd().getTitle(),this.getAd().getType(),this.getAd().getRuntime(), this.getAd().getListingAction(),this.getAd().getDate(),this.getAd().getJobLocation());
        Address address = new Address(this.getAddress().getName(), this.getAddress().getStreet(), this.getAddress().getLocation());
        Item item2 = new Item(null, customer, listingID, ad, name, address);
        return item2;
    }
}
