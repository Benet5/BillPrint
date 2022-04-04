package Billprint.Mapping;

import Billprint.Import.CSVRepo;
import Billprint.Import.Client.Client;
import Billprint.Import.Client.ClientDTO;
import Billprint.Import.Client.ClientRepo;
import Billprint.Import.Client.ClientService;
import Billprint.Import.ImportService;
import Billprint.Import.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MappingService {
    private final ClientService clientService;
    private final ImportService importService;

    public String mappingSelected(String id){
        Optional<Item> item = importService.findById(id);
        if(item.isPresent()) {
            clientMapping(item.get());
            return ("Die Adresse wurde übernommen bzw. neuer Mandant wurde angelegt");
        } throw new RuntimeException("Das Item gibt es nicht.");
    }




    public String mappingAll(){
        List<Item> allItems =importService.findAllMapping();
        List<String> allchanges = new ArrayList();
        for(Item item : allItems){
            allchanges.add(clientMapping(item));
        }
        int createcount = 0;
        int adaptcount = 0;
        for(String change : allchanges){
            if (change.equals("adapted")){
                adaptcount++;
            } else{
                createcount++;
            }
        } return ("Es wurden " +adaptcount+ " Adressen angepasst und " +createcount+ " Mandaten angelegt.");

    }


    public String clientMapping(Item item){
        Optional<Client> actual = clientService.findByAddressName(item.getName()); // worauf abstellen, ganz wichtig ind er Logik! customer oder name? was ist gleich mit adressname?
        if(actual.isPresent()){
            item.setAddress(actual.get().getAddress());
            importService.save(item);
            return "adapted";
        } else{
            Client newClient = new Client();
            newClient.setAddress(item.getAddress());
            newClient.setFee(0);
            newClient.setSkonto(0);
            newClient.setTax(false);
            clientService.createClient(newClient);
            return "created";
        }
    }

}
