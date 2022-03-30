package Billprint.Import.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepo clientRepo;

    public Client createClient(Client client) throws Exception {
        if(clientRepo.findById(client.getId()).isEmpty()){
            return clientRepo.save(client);
        } else {
            throw new Exception("Den Mandanten gibt es schon");
        }

    }

    //prüfen, ob schon vorhanden!

    public List<ClientDTO> findAll(){
        return clientRepo.findAll().stream().map(e -> ClientDTO.of(e)).toList();
    }
}
