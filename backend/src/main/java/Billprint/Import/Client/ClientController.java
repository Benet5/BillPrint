package Billprint.Import.Client;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO client) {
        try{
            if((clientService.findByAddressName(client.toClient())).isEmpty()) {
                Client newClient = clientService.createClient(client.toClient());
                return ResponseEntity.status(201).body(ClientDTO.of(newClient));
            } throw new ClientAlreadyExistsException();

        } catch (ClientAlreadyExistsException e) {
            return ResponseEntity.status(409).build();}
        }


    @GetMapping
    public List<ClientDTO> findAll(){
        return clientService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> changeClient(@RequestBody ClientDTO client, @PathVariable String id){
        if (clientService.findById(id).isPresent()) {
            return ResponseEntity
                    .status(200)
                    .body(clientService.changeClient(id, client.toClient()));
        }
            return ResponseEntity.badRequest().build();
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        clientService.deleteById(id);
    }
}

