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
        ClientToPrintRepo clientToPrintRepo= mock(ClientToPrintRepo.class);
        ClientService clientService = new ClientService(clientRepo);
        ImportService importService = new ImportService(importRepo);

        MappingService mappingService = new MappingService(clientService, importService, clientToPrintRepo);

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
    ClientToPrintRepo clientToPrintRepo = mock(ClientToPrintRepo.class);
    ClientService clientService = new ClientService(clientRepo);
    ImportService importService = new ImportService(importRepo);
    MappingService mappingService = new MappingService(clientService, importService, clientToPrintRepo);

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

    @Test

    void ShouldReturnSum(){
        CSVRepo importRepo = mock(CSVRepo.class);
        ClientRepo clientRepo = mock(ClientRepo.class);
        ClientToPrintRepo clientToPrintRepo = mock(ClientToPrintRepo.class);
        ClientService clientService = new ClientService(clientRepo);
        ImportService importService = new ImportService(importRepo);
        MappingService mappingService = new MappingService(clientService, importService, clientToPrintRepo);

        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "First Offer", "25.10.2022", "Hamburg");
        Ad ad = adDTO.toAd();
        Address address = new Address("OTTO GmBH & Co KG", "Werner-Otto-Straße 1-7", "Hamburg");
        Address address1 = new Address("OSSG", "Werner-Otto-Straße 1-7", "Hamburg");
        Item newItem1 = new Item("12344","OTTO", "bla", ad, "OTTO GmBH & Co KG", address);
        Item newItem2 = new Item("12345", "OTTO", "bla", ad, "OTTO GmBH & Co KG", address);
        Item newItem3 = new Item("12344","OSSG", "bla", ad, "OSSG", address1);
        Item newItem4 = new Item("12345", "OSSG", "bla", ad, "OSSG", address1);
        List <Item> items1 = List.of(newItem1, newItem2);
        List <Item> items2 = List.of(newItem3, newItem4);
        when(importRepo.findAllByName("OSSG")).thenReturn(items2);
        when(importRepo.findAllByName("OTTO GmBH & Co KG")).thenReturn(items1);

        Client client1 = new Client ("12345", address, true, 2, 1);
        Client client2 = new Client ("12346", address1, true, 2, 1);
        ClientToPrint clientToPrint1 = new ClientToPrint(client1, importService);
        ClientToPrint clientToPrint2 = new ClientToPrint(client2, importService);
        System.out.println(clientToPrint1);

        when(clientRepo.findAll()).thenReturn(List.of(client1, client2));
        when(clientToPrintRepo.save(clientToPrint1)).thenReturn(clientToPrint1);
        when(clientToPrintRepo.save(clientToPrint2)).thenReturn(clientToPrint2);


        List<ClientToPrint> actual = mappingService.convertClient();

        assertEquals(2, actual.size());
        assertEquals(2.0, actual.get(0).getFee());
        assertEquals(24.0, actual.get(1).getBrutto());

    }

    @Test

    void ShouldReturnAChangedItem(){
        CSVRepo importRepo = mock(CSVRepo.class);
        ClientRepo clientRepo = mock(ClientRepo.class);
        ClientToPrintRepo clientToPrintRepo = mock(ClientToPrintRepo.class);
        ClientService clientService = new ClientService(clientRepo);
        ImportService importService = new ImportService(importRepo);
        MappingService mappingService = new MappingService(clientService, importService, clientToPrintRepo);

        AdDTO adDTO = new AdDTO("Developer", "Professional", 30, "First Offer", "25.10.2022", "Hamburg");
        Ad ad = adDTO.toAd();
        Address address = new Address("OTTO GmBH & Co KG", "Werner-Otto-Straße 1-7", "Hamburg");
        Item newItem1 = new Item("12344","OTTO", "bla", ad, "OTTO GmBH & Co KG", address);
        Item newItem2 = new Item("12345", "OTTO", "bla", ad, "OTTO GmBH & Co KG", address);
        List <Item> items1 = List.of(newItem1, newItem2);


        Client clientDB = new Client ("12345", address, true, 2, 1);
        Client clientToUpdate = new Client ("12345", address, true, 3, 0);
        Client updatedClient = new Client ("12345", address, true, 3, 0);
        when(clientRepo.findAll()).thenReturn(List.of(clientToUpdate));
        when(importRepo.findAllByName("OTTO GmBH & Co KG")).thenReturn(items1);

        ClientToPrint clientToPrintDB = new ClientToPrint(clientDB, importService);
        ClientToPrint clientToPrintUpdate = new ClientToPrint(clientToUpdate, importService);
        ClientToPrint updatedClientToPrint = new ClientToPrint(updatedClient, importService);


        when(clientToPrintRepo.findByAddressName("OTTO GmBH & Co KG")).thenReturn(Optional.of(clientToPrintDB));
        when(clientToPrintRepo.save(clientToPrintUpdate)).thenReturn(updatedClientToPrint);

        List <ClientToPrint> actual = mappingService.convertClient();

        assertEquals(1, actual.size());
        assertEquals(actual.get(0).getCalcFee(), 0.6);
        assertEquals(actual.get(0).getCalcSkonto(), 0.0);
        assertEquals(actual.get(0).getSumInklSkonto(), 20.0);
        assertEquals(actual.get(0).getSumInklFee(), 20.6);
        assertEquals(actual.get(0).getBrutto(), 24.4);

    }


}