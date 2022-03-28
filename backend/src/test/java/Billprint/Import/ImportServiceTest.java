package Billprint.Import;

import Billprint.Import.Item.Ad;
import Billprint.Import.Item.Address;
import Billprint.Import.Item.Item;

import Billprint.Import.Item.ItemDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        Ad ad = new Ad("Developer", "Professional", 30, "Refund", null, "Hamburg");
        Address address = new Address("OTTO", "Werner-Otto-Straße 1-7", "Hamburg");
        Item newItem1 = new Item("12344","OTTO", "bla", ad, "OTTO GmBH & Co KG", address);
        Item newItem2 = new Item("12345", "OSSG", "bla", ad, "OSSG", address);

        when(csvRepo.findAll()).thenReturn(List.of(newItem2, newItem1));
        List<ItemDTO> actual = importService.getImportedData();

        Assertions.assertTrue(actual.size() == 2);
        Assertions.assertEquals(actual.get(0).getCustomer(), "OSSG");
    }

}