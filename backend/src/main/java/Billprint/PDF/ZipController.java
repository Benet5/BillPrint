package Billprint.PDF;
import Billprint.Mapping.ClientToPrint;
import Billprint.Mapping.MappingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
@RequestMapping("/api/zip")
@RequiredArgsConstructor
public class ZipController {
    private static final Logger LOG = LoggerFactory.getLogger(ZipController.class);

    private final ZipFileService zipFileService;
    private final MappingService mappingService;



    private static final Logger LOGGER = LoggerFactory.getLogger(PDFGeneratorController.class);
    @GetMapping
    public void download(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=Invoices.zip");
        response.setStatus(HttpServletResponse.SC_OK);

        List<ClientToPrint> allClientToPrint = mappingService.findAll();

        try (ZipOutputStream zip = new ZipOutputStream(response.getOutputStream())) {
            for(ClientToPrint client :allClientToPrint) {
                try {
                    ZipEntry zipEntry = new ZipEntry(client.getAddress().getName()+".pdf");
                    zip.putNextEntry(zipEntry);
                    zipFileService.addFile(zip, client);
                    LOGGER.info("PDF erstellt");
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
