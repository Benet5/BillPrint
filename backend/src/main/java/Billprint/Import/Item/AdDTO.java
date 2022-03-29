package Billprint.Import.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {

        private String title;
        private String type;
        private int runtime;
        private String listingAction;
        private String date;
        private String jobLocation;

    public static AdDTO of(Ad ad) {
        return new AdDTO(
                ad.getTitle(),
               ad.getType(),
                ad.getRuntime(),
                ad.getListingAction(),
                ad.dateToString(ad.getDate()),
                ad.getJobLocation()

        );
    }


    public Date toDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        Date newDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return newDate;
    }

    public Ad toAd(){
        return new Ad(this.getTitle(),this.getType(),this.getRuntime(), this.getListingAction(),this.toDate(getDate()),this.getJobLocation());
    }

    }
