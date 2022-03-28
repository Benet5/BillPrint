package Billprint.Import;

import Billprint.Import.Item.CSVItem;
import Billprint.Import.Item.Item;
import Billprint.Import.Item.ItemDTO;
import lombok.RequiredArgsConstructor;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ImportService {
    private final CSVRepo csvRepo;

    public Item createItem(Item item) {
        Item saved = csvRepo.save(item);
        return saved;
    }


    public ImportStatus createItems(InputStream content) {
        try (Reader reader = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8))) {
            CsvToBean<CSVItem> csvToBean = new CsvToBeanBuilder<CSVItem>(reader)
                    .withType(CSVItem.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            csvToBean.parse().stream()
                    .map(item -> item.toItem())
                    .forEach(item -> createItem(item));

            return ImportStatus.SUCCESS;
        } catch (IllegalStateException | IllegalArgumentException | IOException e) {
            return ImportStatus.FAILURE;
        }
    }

    public List<ItemDTO> getImportedData(){
        return csvRepo.findAll().stream()
                .map(item -> ItemDTO.of(item)).toList();
    }


}
