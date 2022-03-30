package Billprint.Import.Client;

import Billprint.Import.Item.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientServiceTest {

    @Test

    void ShouldAddAClients() throws Exception {
        ClientRepo clientRepo = mock(ClientRepo.class);
        ClientService clientService = new ClientService(clientRepo);

        Address address1 = new Address("Würstchenbude", "Heimathafen 1", "225005 Helgoland");
        Client client1 = new Client ("12345", address1, true, 3, 1);


        when(clientRepo.save(client1)).thenReturn(new Client("12345", address1, true, 3, 1));
        Client actual1= clientService.createClient(client1);

        Assertions.assertEquals(actual1.getId(), client1.getId());
    }

    @Test

    void ShouldGetAllClient(){
        ClientRepo clientRepo = mock(ClientRepo.class);
        ClientService clientService = new ClientService(clientRepo);

        Address address1 = new Address("Würstchenbude", "Heimathafen 1", "225005 Helgoland");
        Address address2 = new Address("Würstchenbude2", "Heimathafen 1", "225005 Helgoland");
        Client client1 = new Client ("12345", address1, true, 3, 1);
        Client client2 = new Client ("12346", address2, true, 2, 1);


        ClientDTO clientDTO1 = new ClientDTO ("Würstchenbude", "Heimathafen 1", "225005 Helgoland", true, 3, 1, null);
        ClientDTO clientDTO2 = new ClientDTO  ("Würstchenbude2", "Heimathafen 1", "225005 Helgoland", true, 2, 1, null);


        when(clientRepo.findAll()).thenReturn(List.of(client1, client2));
        List<ClientDTO> list = clientService.findAll();

        Assertions.assertEquals(list.size(), 2);
        assertThat(list.contains(clientDTO1));
        Assertions.assertEquals(list.get(1).getName(), "Würstchenbude2");
    }


}