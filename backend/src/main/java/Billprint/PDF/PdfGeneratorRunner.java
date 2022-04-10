package Billprint.PDF;

import Billprint.Import.Item.Address;
import Billprint.Import.Item.Item;
import Billprint.Mapping.ClientToPrint;
import Billprint.Mapping.MappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class PdfGeneratorRunner {


    @Autowired
    private PdfGenerateService pdfGenerateService;


    public void run(ClientToPrint clientToPrint, OutputStream out) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("clientToPrint", (clientToPrint));
        List<Item> allItemsFromClient = clientToPrint.getAllItemsFromClient();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateNow = dtf.format(LocalDateTime.now());
        String addressName = clientToPrint.getAddress().getName();
        String addressStreet = clientToPrint.getAddress().getStreet();
        String addressLocation =clientToPrint.getAddress().getLocation();
        double netto =clientToPrint.getNetto();
        double calcSkonto = clientToPrint.getCalcSkonto();
        double calcFee = clientToPrint.getCalcFee();
        double calcTax = clientToPrint.getCalcTax();
        double sumInklSkonto = clientToPrint.getSumInklSkonto();
        double sumInklFee = clientToPrint.getSumInklFee();
        double brutto = clientToPrint.getBrutto();
        data.put("dateNow", dateNow);
        data.put("netto", netto);
        data.put("addressName", addressName);
        data.put("addressStreet", addressStreet);
        data.put("addressLocation", addressLocation);
        data.put("calcSkonto", calcSkonto);
        data.put("calcFee", calcFee);
        data.put("calcTax", calcTax);
        data.put("sumInklSkonto", sumInklSkonto);
        data.put("sumInklFee", sumInklFee);
        data.put("brutto", brutto);
        data.put("allItemsFromClient", allItemsFromClient);

        pdfGenerateService.generatePdfFile("generate", data, clientToPrint.getAddress().getName()+".pdf", out);
    }
}


