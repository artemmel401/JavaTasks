package ru.artem.NauJava.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import ru.artem.NauJava.services.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@Tag(name = "report-entity-controller")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate")
    public Long generateReport() {
        Long reportId = reportService.createReport();
        reportService.generateReportAsync(reportId);
        return reportId;
    }

    @GetMapping(path = "/{reportId}", produces = MediaType.TEXT_HTML_VALUE)
    public String getReportById(@PathVariable Long reportId) {
        try {
            return "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<title>Отчет</title>" +
                    "</head>" +
                    "<body>" + reportService.getReportContent(reportId) + "</body>" +
                    "</html>";
        } catch (Exception e) {
            return "<html><body><h1>Ошибка: " + e.getMessage() + "</h1></body></html>";
        }
    }
}
