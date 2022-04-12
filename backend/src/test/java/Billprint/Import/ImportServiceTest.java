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

        Ad ad = new Ad("Developer", "Professional", 30, "Refund", null, "Hamburg");
        Address address = new Address("OTTO", "Werner-Otto-Straße 1-7", "Hamburg");
        Item newItem1 = new Item("12344","OTTO", "bla", ad, "OTTO GmBH & Co KG", address);

        when(csvRepo.save(newItem1)).thenReturn(new Item("12344", null, null, null, null, null));
       Item actual = importService.createItem(newItem1);

        Assertions.assertEquals(actual.getId(), newItem1.getId());
    }

    @Test
    void ShouldGetAllItems(){
        CSVRepo csvRepo = mock(CSVRepo.class);
        ImportService importService = new ImportService(csvRepo);

        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Hamburg");
        Ad ad = adDTO.toAd();
        Address address = new Address("OTTO", "Werner-Otto-Straße 1-7", "Hamburg");
        Item newItem1 = new Item("12344","OTTO", "bla", ad, "OTTO GmBH & Co KG", address);
        Item newItem2 = new Item("12345", "OSSG", "bla", ad, "OSSG", address);

        when(csvRepo.findAll()).thenReturn(List.of(newItem2, newItem1));
        List<ItemDTO> actual = importService.getImportedData();

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(actual.get(0).getCustomer(), "OSSG");
    }

    @Test
    void ShouldChangeAnItem(){
        CSVRepo csvRepo = mock(CSVRepo.class);
        ImportService importService = new ImportService(csvRepo);

        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Hamburg");
        Ad ad = adDTO.toAd();
        Address address = new Address("OTTO", "Werner-Otto-Straße 1-7", "Hamburg");
        Item itemDB = new Item("0815","OTTO", "bla", ad, "OTTO GmBH & Co KG", address);
        Item itemToChange = new Item("0815","OSSG", "bla", ad, "OTTO GmBH & Co KG", address);
        Item itemChanged = new Item("0815","OSSG", "bla", ad, "OTTO GmBH & Co KG", address);


        when(csvRepo.findById("0815")).thenReturn(Optional.of(itemDB));
        when(csvRepo.save(itemToChange)).thenReturn(itemChanged);
       ItemDTO actual = importService.changeItem("0815", itemToChange);
       Assertions.assertEquals(actual.toItem().getCustomer(),itemChanged.getCustomer());

    }

    @Test
    void ShouldDeleteAnItem(){
        CSVRepo csvRepo = mock(CSVRepo.class);
        ImportService importService = new ImportService(csvRepo);

        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Hamburg");
        Ad ad = adDTO.toAd();
        Address address = new Address("OTTO", "Werner-Otto-Straße 1-7", "Hamburg");
        Item itemToDelete = new Item("0815","OSSG", "bla", ad, "OTTO GmBH & Co KG", address);

        importService.deleteById("0815");

        verify(csvRepo).deleteById("0815");

    }

    @Test
    void ShoudCreateItems(){
        CSVRepo csvRepo = mock(CSVRepo.class);
        ImportService importService = new ImportService(csvRepo);
        InputStream input = getClass().getResourceAsStream("data.csv");

        importService.createItems(input);
        AdDTO adDTO = new AdDTO("Head of Compliance & Geldwäschebeauftragter (w/m/d) | Payments","Professional", 60, "Offer First Online","26.10.2020","Hamburg Zentrale" );
        Ad ad = adDTO.toAd();
        Address address = new Address("Otto (GmbH & Co KG)","Werner-Otto-Straße 1-7","22179 Hamburg");

        verify(csvRepo).saveAll(List.of(
                new Item(null,"Otto (GmbH & Co KG)","6653722",ad, "Otto (GmbH & Co KG)", address)
        ));


    }


}