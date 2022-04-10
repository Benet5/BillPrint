package Billprint.PDF;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

public interface PdfGenerateService {
    void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName, OutputStream out);
}