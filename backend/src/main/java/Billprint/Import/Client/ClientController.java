package Billprint.Import.Client;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO client) {
            if((clientService.findByAddressName(client.toClient().getAddress().getName())).isEmpty()) {
                Client newClient = clientService.createClient(client.toClient());
                return ResponseEntity.status(201).body(ClientDTO.of(newClient));
            } else {
                return ResponseEntity.status(409).build();
            }
        }



    @GetMapping
    public List<ClientDTO> findAll(){
        return clientService.findAll().stream().map(e -> ClientDTO.of(e)).toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> changeClient(@RequestBody ClientDTO client, @PathVariable String id){
        if(clientService.findById(id).isPresent()){
            return ResponseEntity.of(clientService.findById(id)
                    .map(foundClient -> clientService.changeClient(id, client.toClient())));
        } else{
            return ResponseEntity.status(409).build();
        }
    }



    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        clientService.deleteById(id);
    }
}

