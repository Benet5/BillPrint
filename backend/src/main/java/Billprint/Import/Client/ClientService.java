package Billprint.Import.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepo clientRepo;

    public Client createClient(Client client)  {
        return clientRepo.save(client);
        }


    public List<ClientDTO> findAll(){
        return clientRepo.findAll().stream().map(e -> ClientDTO.of(e)).toList();
    }

    public Optional<Client> findById(String id){
        return clientRepo.findById(id);
}

public ClientDTO changeClient(String id, Client client){
    Optional<Client> toChange = clientRepo.findById(id);
    toChange.get().setAddress(client.getAddress());
    toChange.get().setFee(client.getFee());
    toChange.get().setSkonto(client.getSkonto());
    toChange.get().setTax(client.isTax());
    clientRepo.save(toChange.get());
    return ClientDTO.of(toChange.get());
}

public void deleteById(String id){
        clientRepo.deleteById(id);
}


public Optional<Client>findByAddressName(Client client){
        return (clientRepo.findByAddressName(client.getAddress().getName()));
}
}
