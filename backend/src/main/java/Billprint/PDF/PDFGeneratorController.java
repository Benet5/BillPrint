package Billprint.PDF;

import Billprint.Mapping.ClientToPrint;
import Billprint.Mapping.MappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PDFGeneratorController {

private final PdfGeneratorRunner pdfGeneratorRunner;
private final MappingService mappingService;

    @GetMapping("/generate")
    public void generateAll(){
        List<ClientToPrint> allClientToPrint = mappingService.findAll();
        for(ClientToPrint client :allClientToPrint)
            try{
                pdfGeneratorRunner.run(client);
            } catch(Exception e){
                e.getMessage();
            }
    }



}


