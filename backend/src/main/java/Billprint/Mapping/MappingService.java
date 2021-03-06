package Billprint.Mapping;

import Billprint.Import.Client.Client;

import Billprint.Import.Client.ClientService;
import Billprint.Import.ImportService;
import Billprint.Import.Item.Address;
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
    private final ClientToPrintRepo clientToPrintRepo;

    public String mappingSelected(String id){
        Optional<Item> item = importService.findById(id);
        if(item.isPresent()) {
            clientMapping(item.get());
            return ("Die Adresse wurde ├╝bernommen bzw. neuer Mandant wurde angelegt");
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
            Address address = new Address();
            address.setName(item.getName());
            address.setStreet(item.getAddress().getStreet());
            address.setLocation(item.getAddress().getLocation());
            Client newClient = new Client();
            newClient.setAddress(address);
            newClient.setFee(0);
            newClient.setSkonto(0);
            newClient.setTax(false);
            clientService.createClient(newClient);
            return "created";
        }
    }

        public List<ClientToPrint> convertClient(){
        List<Client> allClients = clientService.findAll();
        List<ClientToPrint> allConverted = new ArrayList<>();
        for(Client client :allClients) {
            Optional<ClientToPrint> toChange = clientToPrintRepo.findByAddressName(client.getAddress().getName());
            if(toChange.isEmpty()) {
                ClientToPrint clientToPrint = new ClientToPrint(client, this.importService);
                ClientToPrint actual = clientToPrintRepo.save(clientToPrint);
                allConverted.add(actual);
            } else{
                allConverted.add(changeClientToPrint(client, toChange.get()));
            }

            }return allConverted;

        }


    public ClientToPrint changeClientToPrint(Client client, ClientToPrint toChange){
        toChange.setAddress(client.getAddress());
        toChange.setFee(client.getFee());
        toChange.setSkonto(client.getSkonto());
        toChange.setTax(client.isTax());
        toChange.setAllItemsFromClient(importService.findAllByName(client.getAddress().getName()));
        toChange.setNetto(toChange.calcNetto());
        toChange.setCalcSkonto(toChange.calcSkonto());
        toChange.setCalcFee(toChange.calcFee());
        toChange.setCalcTax(toChange.calcTax());
        toChange.setSumInklSkonto(toChange.calcSumInklSkonto());
        toChange.setSumInklFee(toChange.calcSumInklFee());
        toChange.setBrutto(toChange.calcbrutto());

        return clientToPrintRepo.save(toChange);
    }




    public List<ClientToPrint> findAll(){
        return clientToPrintRepo.findAll();
    }



}
