package Billprint.Mapping;

import Billprint.Import.CSVRepo;
import Billprint.Import.Client.Client;
import Billprint.Import.Client.ClientRepo;
import Billprint.Import.Client.ClientService;
import Billprint.Import.ImportService;
import Billprint.Import.Item.Ad;
import Billprint.Import.Item.AdDTO;
import Billprint.Import.Item.Address;
import Billprint.Import.Item.Item;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

class MappingServiceTest {

    @Test

            void ShouldMappByAdressName()
    {

        CSVRepo importRepo = mock(CSVRepo.class);
        ClientRepo clientRepo = mock(ClientRepo.class);
        ClientService clientService = new ClientService(clientRepo);
        ImportService importService = new ImportService(importRepo);

        MappingService mappingService = new MappingService(clientService, importService);

        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Hamburg");
        Ad ad = adDTO.toAd();
        Address address = new Address("OSSG", "Werner-Otto-Straße 45", "0815 Hamburg");
        Item newItem1 = new Item("12344", "OTTO GmBH & Co KG", "bla", ad, "OTTO GmBH & Co KG", address);
        Item newItem2 = new Item("12345", "OSSG", "bla", ad, "OSSG", address);


        Address address1 = new Address("OTTO GmBH & Co KG", "Werner-Otto-Straße 1-7", "Hamburg");
        Client clientDB = new Client("12345", address1, true, 3, 1);
        Client newClient = new Client(null, address, false, 0,0);

        when(importRepo.findAll()).thenReturn(List.of(newItem2, newItem1));
        when(clientRepo.findByAddressName("OTTO GmBH & Co KG")).thenReturn(Optional.of(clientDB));
        when(importRepo.save(newItem1)).thenReturn(newItem1);
        when(clientService.createClient(newClient)).thenReturn(newClient);

        String actual = mappingService.mappingAll();
        assertEquals("Es wurden 1 Adressen angepasst und 1 Mandaten angelegt.", actual);
       assertTrue(newItem1.getAddress().getStreet().equals("Werner-Otto-Straße 1-7"));
       verify(clientRepo).save(newClient);


    }

@Test

    void ShouldMapSelected(){
    CSVRepo importRepo = mock(CSVRepo.class);
    ClientRepo clientRepo = mock(ClientRepo.class);
    ClientService clientService = new ClientService(clientRepo);
    ImportService importService = new ImportService(importRepo);
    MappingService mappingService = new MappingService(clientService, importService);

    AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "Refund", "25.10.2022", "Hamburg");
    Ad ad = adDTO.toAd();
    Address address1 = new Address("OTTO GmBH & Co KG", "Werner-Otto-Straße 1-7", "Hamburg");
    Address address = new Address("OSSG", "Werner-Otto-Straße 45", "0815 Hamburg");
    Client clientDB = new Client("12345", address1, true, 3, 1);
    Item newItem3 = new Item("12344", "OTTO GmBH & Co KG", "bla", ad, "OTTO GmBH & Co KG", address);

    when(importRepo.findById("12344")).thenReturn(Optional.of(newItem3));
    when(clientRepo.findByAddressName("OTTO GmBH & Co KG")).thenReturn(Optional.of(clientDB));
    when(importRepo.save(newItem3)).thenReturn(newItem3);

    mappingService.mappingSelected("12344");

    assertTrue(newItem3.getAddress().getStreet().equals("Werner-Otto-Straße 1-7"));
    verify(importRepo).save(newItem3);


}



}