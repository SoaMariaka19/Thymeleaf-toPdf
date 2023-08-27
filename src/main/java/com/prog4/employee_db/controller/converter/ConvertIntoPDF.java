package com.prog4.employee_db.controller.converter;

import com.lowagie.text.DocumentException;
import com.prog4.employee_db.controller.model.ModelEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.rmi.ServerException;
import java.util.Map;

import static org.thymeleaf.templatemode.TemplateMode.HTML;
@Component
public class ConvertIntoPDF {
    private static final String EMPLOYEE_HTML_TEMPLATE = "employee_fiche";

    public byte[] generatePdf(ModelEmployee employee, String template) throws ServerException {
        ITextRenderer renderer = new ITextRenderer();
        loadStyle(renderer, employee, template);
        renderer.layout();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            renderer.createPDF(outputStream);
        } catch (DocumentException e) {
            throw new ServerException("Pdf generation ended with exception :" + e);
        }
        return outputStream.toByteArray();
    }

    private void loadStyle(ITextRenderer renderer, ModelEmployee employee, String template) {
        renderer.setDocumentFromString(parseCardTemplateToString(employee, template));

    }

    private String parseCardTemplateToString(
            ModelEmployee employee, String template) {
        TemplateEngine templateEngine = configureTemplate();
        Context context = configureContext(employee);
        return templateEngine.process(template, context);
    }

    private Context configureContext(ModelEmployee employee) {
        Context context = new Context();
        context.setVariable("employee", employee);
        return context;
    }

    private TemplateEngine configureTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/employee/pdf/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    public byte[] getPdfCard(ModelEmployee employee) throws ServerException {
        return generatePdf(employee, EMPLOYEE_HTML_TEMPLATE);
    }
}
