package Billprint.Import;

import Billprint.Import.Item.CSVItem;
import Billprint.Import.Item.Item;
import lombok.RequiredArgsConstructor;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

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


}
