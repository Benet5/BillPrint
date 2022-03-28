package Billprint.Import;


import Billprint.Import.Item.CSVItem;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/import")
@RequiredArgsConstructor

public class ImportController {

    private final ImportService importService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSVItem> createItem(@RequestBody CSVItem item) {
        return ResponseEntity
                .status(201)
                .body(CSVItem.of(importService.createItem(item.toItem())));
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
}
