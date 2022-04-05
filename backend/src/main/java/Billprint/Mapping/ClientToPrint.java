package Billprint.Mapping;


import Billprint.Import.CSVRepo;
import Billprint.Import.Client.Client;

import Billprint.Import.ImportService;
import Billprint.Import.Item.Address;
import Billprint.Import.Item.Item;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Data
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
        this.sumInklFee = calcsumInklFee();
        this.brutto = calcbrutto();
    }


    private double calcNetto() {
        double sum = 0;
        for (Item item : allItemsFromClient) {
            if (item.getAd().getType().equals("Professional")) {
                 sum += 10.0;
            } else {
                 sum += 5.0;
            }
        } return sum;
    }

    private double calcSkonto() {
        double doubleSkonto = skonto;
        return netto * (doubleSkonto / 100);
    }

    private double calcFee() {
        double doubleFee = fee;
        return netto * (doubleFee / 100);
    }

    private double calcTax() {
        if (tax) {
            return netto * 0.19;
        } else return 0;
    }

    private double round(double d) {
        return (Math.round(d*100))/100;
    }

    private double calcSumInklSkonto(){
        return round(netto-calcSkonto);
    }

    private double calcsumInklFee(){
        return round(sumInklSkonto + calcFee);
    }

    private double calcbrutto(){
        return round(sumInklFee + calcTax);
    }


}
