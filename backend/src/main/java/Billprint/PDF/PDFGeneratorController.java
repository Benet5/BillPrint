package Billprint.PDF;

import Billprint.Mapping.ClientToPrint;
import Billprint.Mapping.MappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.util.List;

@Controller
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/generate")
public class PDFGeneratorController {

private final PdfGeneratorRunner pdfGeneratorRunner;
private final MappingService mappingService;

    @PutMapping
    public void generateAll(){
        List<ClientToPrint> allClientToPrint = mappingService.findAll();
        for(ClientToPrint client :allClientToPrint) {
            try {
                pdfGeneratorRunner.run(client);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }




}


