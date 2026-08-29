package com.datascience.web;

import com.datascience.service.BasketStatisticsExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BasketStatisticsExportController {

    private static final String EXPORT_PATH = "/basket-statistics-export";

    private final BasketStatisticsExportService exportService;

    @GetMapping(EXPORT_PATH)
    public String exportPage() {
        return "basket-statistics-export";
    }

    @PostMapping(EXPORT_PATH)
    public String export(RedirectAttributes redirectAttributes) {
        try {
            BasketStatisticsExportService.ExportResult result = exportService.exportStatistics();
            redirectAttributes.addFlashAttribute("message", "Basket statistics exported");
            redirectAttributes.addFlashAttribute("result", result);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:" + EXPORT_PATH;
    }
}
