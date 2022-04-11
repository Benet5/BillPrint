package Billprint.PDF;

import com.lowagie.text.DocumentException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;


import java.io.*;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
@Service
public class PdfGenerateServiceImpl implements PdfGenerateService{
    private Logger logger = LoggerFactory.getLogger(PdfGenerateServiceImpl.class);

    @Autowired
    private TemplateEngine templateEngine;

    //@Value("${pdf.directory}")
    //private final String pdfDirectory = FileOutputStream + File.separator;

    @Override
    public void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName, OutputStream out)  {
       createFile(templateName, data, pdfFileName);
       zippen(pdfFileName, out);
    }

    public void createFile(String templateName, Map<String, Object> data, String pdfFileName){
        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process(templateName, context);
        try(FileOutputStream fileOutputStream = new FileOutputStream(pdfFileName)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void zippen(String pdfFileName, OutputStream out){
        try( FileInputStream fileInputStream = new FileInputStream(pdfFileName)) {
            out.write(fileInputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.deleteIfExists(Path.of(pdfFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    }




