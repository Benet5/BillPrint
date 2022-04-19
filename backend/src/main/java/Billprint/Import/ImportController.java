package Billprint.Import;


import Billprint.Import.Item.ItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController

@RequestMapping("/api/import")
@RequiredArgsConstructor

public class ImportController {

    private final ImportService importService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO item) {
        return ResponseEntity
                .status(201)
                .body(ItemDTO.of(importService.createItem(item.toItem())));
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createItems(@RequestParam("csv") MultipartFile file) throws IOException {
        ImportStatus importStatus = importService.createItems(file.getInputStream());
        if (importStatus == ImportStatus.SUCCESS) {
            return ResponseEntity.ok().build();
        } else if (importStatus == ImportStatus.PARTIAL) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping
    public List<ItemDTO> getImportedData() {
        return importService.getImportedData();
    }


    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> changeItem(@PathVariable String id, @RequestBody ItemDTO item) {
        if (importService.findById(id).isPresent()) {
            return ResponseEntity
                    .status(200)
                    .body(importService.changeItem(id, item.toItem()));
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable String id) {
        importService.deleteById(id);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        importService.deleteAllData();
        return ResponseEntity.status(200).build();
    }


}