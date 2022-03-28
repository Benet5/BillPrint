package Billprint.Import.Item;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CSVItem {



    @CsvBindByName
    private String name;

    @CsvBindByName
    private String listingID;

    @CsvBindByName
    private String title;
    @CsvBindByName
    private String type;
    @CsvBindByName
    private int runtime;
    @CsvBindByName
    private String listingAction;

    @CsvBindByName
    private String date;

    public static Date toDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        Date newDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return newDate;
    }


    @CsvBindByName
    private String jobLocation;

    @CsvBindByName
    private String customer;

    @CsvBindByName
    private String addressName;
    @CsvBindByName
    private String addressStreet;
    @CsvBindByName
    private String addressLocation;




public static CSVItem of(Item item){

    return new CSVItem(
            item.getName(),
            item.getListingID(),
            item.getAd().getTitle(),
            item.getAd().getType(),
            item.getAd().getRuntime(),
            item.getAd().getListingAction(),
            item.getAd().getJobLocation(),
            item.getAd().dateToString(item.getAd().getDate()),
            item.getCustomer(),
            item.getAddress().getName(),
            item.getAddress().getStreet(),
            item.getAddress().getLocation()
    );
}



public Item toItem(){
    Ad ad =new Ad(title, type, runtime, listingAction,CSVItem.toDate(date),jobLocation);
    Address address = new Address(addressName, addressStreet, addressLocation);
    Item item = new Item(null, customer, listingID, ad, name, address);
    return item;
}

}
