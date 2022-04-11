package Billprint.PDF;

import Billprint.Mapping.ClientToPrint;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ZipFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PDFGeneratorController.class);
    private final PdfGeneratorRunner pdfGeneratorRunner;

    public void addFile(OutputStream out, ClientToPrint client) throws IOException {
        try {
            pdfGeneratorRunner.run(client, out);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}
