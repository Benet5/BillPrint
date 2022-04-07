package Billprint.Mapping;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Put;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/mapping")
@RequiredArgsConstructor
public class MappingController {

    private final MappingService mappingService;


    @PutMapping
    public String mappingAll(){
        return mappingService.mappingAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> mappingSelected(@PathVariable String id) {
        String result = mappingService.mappingSelected(id);
        if (result.equals("Das Item gibt es nicht.")) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(200).build();
        }
    }

    @PutMapping("/convert")
    public void convertClients(){
        mappingService.convertClient();
    }

    @GetMapping
    public List<ClientToPrint> findall(){
        return mappingService.findAll();
    }
}
