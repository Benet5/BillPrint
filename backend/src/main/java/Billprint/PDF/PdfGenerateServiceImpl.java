package Billprint.PDF;

import com.lowagie.text.DocumentException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;
@Service
public class PdfGenerateServiceImpl implements PdfGenerateService{
    private Logger logger = LoggerFactory.getLogger(PdfGenerateServiceImpl.class);

    @Autowired
    private TemplateEngine templateEngine;

    //@Value("${pdf.directory}")
    //private final String pdfDirectory = System.getProperty("user.home") + File.separator;

// alternative: try mit resources FileOutputStream fileOutputStream = new FileOutputStream(pdfDirectory + pdfFileName)
    @Override
    public void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName, OutputStream out)  {
        Context context = new Context();
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);

        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(out, false);
           renderer.finishPDF();

        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
        }
    }
    }



