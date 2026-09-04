package com.datascience.web;

import com.datascience.service.BasketVariationMappingAdminService;
import com.datascience.service.BasketVariationMappingAdminService.CellSubmission;
import com.datascience.service.BasketVariationMappingAdminService.MappingFormSubmission;
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
    private static final String ROUND_BASKET_COURSE_PREFIX = "roundBasketCourse_";
    private static final String ROUND_SAME_LAYOUT_PREFIX = "roundSameLayout_";
    private static final String ROUND_COURSE_SELECTION_BY_GROUP_PREFIX = "roundCourseSelectionByGroup_";
    private static final String GROUP_BASKET_COURSE_PREFIX = "groupBasketCourse_";

    private final BasketVariationMappingAdminService mappingAdminService;

    @GetMapping(MAPPING_PATH)
    public String mappingEditor(
            @RequestParam(value = "competitionId", required = false) Long competitionId,
            Model model
    ) {
        MappingEditorModel editor = mappingAdminService.loadEditor(competitionId);
        model.addAttribute("editor", editor);
        return "basket-variation-mappings";
    }

    @PostMapping(MAPPING_PATH)
    public String saveMappings(
            @RequestParam("competitionId") Long competitionId,
            @RequestParam Map<String, String> parameters,
        RedirectAttributes redirectAttributes
    ) {
        try {
            mappingAdminService.saveMappings(competitionId, parseFormSubmission(parameters), parseSubmissions(parameters));
            redirectAttributes.addFlashAttribute("message", "Basket variation mappings saved");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return redirectToEditor(competitionId);
    }

    private MappingFormSubmission parseFormSubmission(Map<String, String> parameters) {
        Long basketCourseId = parseOptionalLong(parameters.get("basketCourseId"), "Invalid basket course submitted");
        boolean sameLayout = "true".equals(parameters.get("sameLayout"));
        boolean courseSelectionByRound = "true".equals(parameters.get("courseSelectionByRound"));
        return new MappingFormSubmission(
                basketCourseId,
                sameLayout,
                courseSelectionByRound,
                parseLongMap(parameters, ROUND_BASKET_COURSE_PREFIX, "Invalid round setting submitted", "Invalid basket course submitted"),
                parseBooleanMap(parameters, ROUND_SAME_LAYOUT_PREFIX, "Invalid round setting submitted"),
                parseBooleanMap(parameters, ROUND_COURSE_SELECTION_BY_GROUP_PREFIX, "Invalid round setting submitted"),
                parseLongMap(parameters, GROUP_BASKET_COURSE_PREFIX, "Invalid group setting submitted", "Invalid basket course submitted")
        );
    }

    private Map<Long, Long> parseLongMap(
            Map<String, String> parameters,
            String prefix,
            String keyMessage,
            String valueMessage
    ) {
        Map<Long, Long> values = new java.util.LinkedHashMap<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (!entry.getKey().startsWith(prefix)) {
                continue;
            }
            Long id = parseLong(entry.getKey().substring(prefix.length()), keyMessage);
            values.put(id, parseOptionalLong(entry.getValue(), valueMessage));
        }
        return values;
    }

    private Map<Long, Boolean> parseBooleanMap(Map<String, String> parameters, String prefix, String keyMessage) {
        Map<Long, Boolean> values = new java.util.LinkedHashMap<>();
        for (String key : parameters.keySet()) {
            if (key.startsWith(prefix)) {
                values.put(parseLong(key.substring(prefix.length()), keyMessage), true);
            }
        }
        return values;
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
            Long basketVariationId = parseOptionalLong(entry.getValue(), "Invalid basket variation submitted");
            submissions.add(new CellSubmission(roundDivisionId, holeOrdinal, basketVariationId));
        }
        return submissions;
    }

    private Long parseOptionalLong(String value, String message) {
        return value == null || value.isBlank() ? null : parseLong(value, message);
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

    private String redirectToEditor(Long competitionId) {
        return "redirect:" + MAPPING_PATH + "?competitionId=" + competitionId;
    }
}
