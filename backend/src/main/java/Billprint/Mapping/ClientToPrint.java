package Billprint.Mapping;


import Billprint.Import.CSVRepo;
import Billprint.Import.Client.Client;

import Billprint.Import.ImportService;
import Billprint.Import.Item.Address;
import Billprint.Import.Item.Item;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Data
@NoArgsConstructor
@Document("ClientToPrint")
public class ClientToPrint {


    @Id
    private String id;
    private Address address;
    private boolean tax;
    private int fee;
    private int skonto;
    private List<Item> allItemsFromClient;
    private double netto;
    private double calcSkonto;
    private double calcFee;
    private double calcTax;
    private double sumInklSkonto;
    private double sumInklFee;
    private double brutto;





    public ClientToPrint(Client client, ImportService importService) {
        this.address = client.getAddress();
        this.fee = client.getFee();
        this.tax = client.isTax();
        this.skonto = client.getSkonto();
        this.allItemsFromClient = importService.findAllByName(client.getAddress().getName());
        this.netto = calcNetto();
        this.calcSkonto = calcSkonto();
        this.calcFee = calcFee();
        this.calcTax = calcTax();
        this.sumInklSkonto =calcSumInklSkonto();
        this.sumInklFee = calcSumInklFee();
        this.brutto = calcbrutto();
    }


    double calcNetto() {
        double sum = 0.0;
        for (Item item : allItemsFromClient) {
            if (item.getAd().getType().equals("Professional") && !item.getAd().getListingAction().equals("Refund")) {
                 sum += 10.0;
            } else if (item.getAd().getType().equals("Campus") && !item.getAd().getListingAction().equals("Refund")) {
                 sum += 5.0;
            } else if (item.getAd().getType().equals("Professional") && item.getAd().getListingAction().equals("Refund")){
                 sum -= 10.0;
            }else if (item.getAd().getType().equals("Campus") && item.getAd().getListingAction().equals("Refund")){
                sum -= 5.0;
            }
        } return sum;
    }

    double calcSkonto() {
        double doubleSkonto = skonto;
        return Math.abs(round(netto * (doubleSkonto / 100.0)));
    }

    double calcFee() {
        double doubleFee = fee;
        return round(Math.abs(netto * (doubleFee / 100.0)));
    }

    double calcTax() {
        if (tax) {
            return Math.abs(netto * 0.19);
        } else return 0;
    }

    private double round(double d) {
        return (Math.round(d*100.0))/100.0;
    }

    double calcSumInklSkonto(){
        if(netto<0.0){
            return round(netto+calcSkonto);
        }return round(netto-calcSkonto);
    }

    double calcSumInklFee() {
        if (sumInklSkonto < 0.0) {
            return round(sumInklSkonto - calcFee);
        }return round(sumInklSkonto + calcFee);
    }

    double calcbrutto() {
        if (sumInklFee < 0.0) {
            return round(sumInklFee - calcTax);
        }return round(sumInklFee + calcTax);
    }

    private String finalStatement(){
        if (brutto > 0.0){
            return "Bitte überweisen sie den ausstehenden Betrag zeitnah.";
        } return "Der überzählige Betrag wird Ihnen zeitnah erstattet.";
    }


}
