package Billprint.Import;

import Billprint.Import.Item.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ImportServiceTest {

    @Test
    void ShouldAddAnItem(){
        CSVRepo csvRepo = mock(CSVRepo.class);
        ImportService importService = new ImportService(csvRepo);

        Ad ad = new Ad("Developer", "Professional", 30, "Refund", null, "Entenhausen");
        Address address = new Address("Donalds PolierService", "Gansweg 1", "Entenhausen");
        Item newItem1 = new Item("12344","Donalds PolierService", "bla", ad, "Donalds PolierService", address);

        when(csvRepo.save(newItem1)).thenReturn(new Item("12344", null, null, null, null, null));
       Item actual = importService.createItem(newItem1);

        Assertions.assertEquals(actual.getId(), newItem1.getId());
    }

    @Test
    void ShouldGetAllItems(){
        CSVRepo csvRepo = mock(CSVRepo.class);
        ImportService importService = new ImportService(csvRepo);

        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Entenhausen");
        Ad ad = adDTO.toAd();
        Address address = new Address("Donalds PolierService", "Gansweg 1", "Entenhausen");
        Item newItem1 = new Item("12344","Donalds PolierService", "bla", ad, "Donalds PolierService", address);
        Item newItem2 = new Item("12345", "Daisy Gold", "bla", ad, "Daisy Gold", address);

        when(csvRepo.findAll()).thenReturn(List.of(newItem2, newItem1));
        List<ItemDTO> actual = importService.getImportedData();

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(actual.get(0).getCustomer(), "Daisy Gold");
    }

    @Test
    void ShouldChangeAnItem(){
        CSVRepo csvRepo = mock(CSVRepo.class);
        ImportService importService = new ImportService(csvRepo);

        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Entenhausen");
        Ad ad = adDTO.toAd();
        Address address = new Address("Donalds PolierService", "Gansweg 1", "Entenhausen");
        Item itemDB = new Item("0815","Donalds PolierService", "bla", ad, "Donalds PolierService", address);
        Item itemToChange = new Item("0815","Daisy Gold", "bla", ad, "Daisy Gold", address);
        Item itemChanged = new Item("0815","Daisy Gold", "bla", ad, "Daisy Gold", address);


        when(csvRepo.findById("0815")).thenReturn(Optional.of(itemDB));
        when(csvRepo.save(itemToChange)).thenReturn(itemChanged);
       ItemDTO actual = importService.changeItem("0815", itemToChange);
       Assertions.assertEquals(actual.toItem().getCustomer(),itemChanged.getCustomer());

    }

    @Test
    void ShouldDeleteAnItem(){
        CSVRepo csvRepo = mock(CSVRepo.class);
        ImportService importService = new ImportService(csvRepo);

        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Entenhausen");
        Ad ad = adDTO.toAd();
        Address address = new Address("Donalds PolierService", "Gansweg 1", "Entenhausen");
        Item itemToDelete = new Item("0815","Daisy Gold", "bla", ad, "Donalds PolierService", address);

        importService.deleteById("0815");

        verify(csvRepo).deleteById("0815");

    }

    @Test
    void ShoudCreateItems(){
        CSVRepo csvRepo = mock(CSVRepo.class);
        ImportService importService = new ImportService(csvRepo);
        InputStream input = getClass().getResourceAsStream("data.csv");

        importService.createItems(input);
        AdDTO adDTO = new AdDTO("Head of Compliance & Geldwäsche","Professional", 60, "Offer First Online","26.10.2020","Entenhausen Zentrale" );
        Ad ad = adDTO.toAd();
        Address address = new Address("Donalds PolierService","Gansweg 1","22179 Entenhausen");

        verify(csvRepo).saveAll(List.of(
                new Item(null,"Donalds PolierService","6653722",ad, "Donalds PolierService", address)
        ));


    }


}