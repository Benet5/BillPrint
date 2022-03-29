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
import java.util.Optional;


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

            csvRepo.saveAll(csvToBean.parse().stream()
                    .map(item -> item.toItem())
                            .toList());

                    //.forEach(item -> createItem(item));

            return ImportStatus.SUCCESS;
        } catch (IllegalStateException | IllegalArgumentException | IOException e) {
            return ImportStatus.FAILURE;
        }
    }

    public List<ItemDTO> getImportedData(){
        return csvRepo.findAll().stream()
                .map(item -> ItemDTO.of(item)).toList();
    }


    public void deleteAllData(){
        csvRepo.deleteAll();
    }


    public void deleteById(String id){
        csvRepo.deleteById(id);
    }



    public ItemDTO changeItem(String id, Item item){
        Optional<Item> toChange = csvRepo.findById(id);
        if(toChange.isEmpty()){
            throw new RuntimeException("Item ist nicht existent!");
        }else{
            toChange.get().setCustomer(item.getCustomer());
            toChange.get().setListingID(item.getListingID());
            toChange.get().getAd().setTitle(item.getAd().getTitle());
            toChange.get().getAd().setType(item.getAd().getType());
            toChange.get().getAd().setRuntime(item.getAd().getRuntime());
            toChange.get().getAd().setListingAction(item.getAd().getListingAction());
            toChange.get().getAd().setDate(item.getAd().getDate());
            toChange.get().getAd().setJobLocation(item.getAd().getJobLocation());
            toChange.get().setName(item.getName());
            toChange.get().getAddress().setName(item.getAddress().getName());
            toChange.get().getAddress().setStreet(item.getAddress().getStreet());
            toChange.get().getAddress().setLocation(item.getAddress().getLocation());
            csvRepo.save(toChange.get());
        }
        return ItemDTO.of(toChange.get());
    }


}
