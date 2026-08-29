package com.datascience.web;

import com.datascience.service.BasketVariationMappingAdminService;
import com.datascience.service.BasketVariationMappingAdminService.CellSubmission;
import com.datascience.service.BasketVariationMappingAdminService.MappingEditorModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BasketVariationMappingAdminController {

    private static final String MAPPING_PATH = "/basket-variation-mappings";
    private static final String CELL_PREFIX = "cell_";

    private final BasketVariationMappingAdminService mappingAdminService;

    @GetMapping(MAPPING_PATH)
    public String mappingEditor(
            @RequestParam(value = "competitionId", required = false) Long competitionId,
            @RequestParam(value = "basketCourseId", required = false) Long basketCourseId,
            Model model
    ) {
        MappingEditorModel editor = mappingAdminService.loadEditor(competitionId, basketCourseId);
        model.addAttribute("editor", editor);
        return "basket-variation-mappings";
    }

    @PostMapping(MAPPING_PATH)
    public String saveMappings(
            @RequestParam("competitionId") Long competitionId,
            @RequestParam("basketCourseId") Long basketCourseId,
            @RequestParam Map<String, String> parameters,
            RedirectAttributes redirectAttributes
    ) {
        try {
            mappingAdminService.saveMappings(competitionId, basketCourseId, parseSubmissions(parameters));
            redirectAttributes.addFlashAttribute("message", "Basket variation mappings saved");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return redirectToEditor(competitionId, basketCourseId);
    }

    private List<CellSubmission> parseSubmissions(Map<String, String> parameters) {
        List<CellSubmission> submissions = new ArrayList<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (!entry.getKey().startsWith(CELL_PREFIX)) {
                continue;
            }
            String[] parts = entry.getKey().substring(CELL_PREFIX.length()).split("_");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid mapping cell submitted");
            }
            Long roundDivisionId = parseLong(parts[0], "Invalid round division submitted");
            Integer holeOrdinal = parseInteger(parts[1], "Invalid hole ordinal submitted");
            Long basketVariationId = entry.getValue() == null || entry.getValue().isBlank()
                    ? null
                    : parseLong(entry.getValue(), "Invalid basket variation submitted");
            submissions.add(new CellSubmission(roundDivisionId, holeOrdinal, basketVariationId));
        }
        return submissions;
    }

    private Long parseLong(String value, String message) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(message);
        }
    }

    private Integer parseInteger(String value, String message) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(message);
        }
    }

    private String redirectToEditor(Long competitionId, Long basketCourseId) {
        return "redirect:" + MAPPING_PATH + "?competitionId=" + competitionId + "&basketCourseId=" + basketCourseId;
    }
}
