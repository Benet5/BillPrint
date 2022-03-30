package Billprint.Import.Client;

import Billprint.Import.Item.ItemDTO;
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
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO client) throws Exception {
        return ResponseEntity
                .status(201)
                .body(ClientDTO.of(clientService.createClient(client.toClient())));
    }

    @GetMapping
    public List<ClientDTO> findAll(){
        return clientService.findAll();
    }


}

