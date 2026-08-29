package com.datascience.web;

import com.datascience.domain.Basket;
import com.datascience.domain.BasketCourse;
import com.datascience.service.BasketCourseAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basket-courses")
@RequiredArgsConstructor
public class BasketCourseAdminController {

    private final BasketCourseAdminService basketCourseAdminService;

    @GetMapping
    public String courses(Model model) {
        model.addAttribute("courses", basketCourseAdminService.listCourses());
        return "basket-courses";
    }

    @PostMapping
    public String createCourse(@RequestParam("name") String name, RedirectAttributes redirectAttributes) {
        try {
            BasketCourse course = basketCourseAdminService.createCourse(name);
            redirectAttributes.addFlashAttribute("message", "Basket course saved");
            return "redirect:/basket-courses/" + course.getId();
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/basket-courses";
        }
    }

    @GetMapping("/{courseId}")
    public String courseDetail(@PathVariable Long courseId, Model model) {
        BasketCourse course = basketCourseAdminService.getCourse(courseId);
        List<Basket> baskets = basketCourseAdminService.listBaskets(courseId);
        model.addAttribute("course", course);
        model.addAttribute("baskets", baskets);
        model.addAttribute("variationsByBasket", basketCourseAdminService.listVariationsByBasket(courseId));
        return "basket-course-detail";
    }

    @PostMapping("/{courseId}/baskets")
    public String createBasket(
            @PathVariable Long courseId,
            @RequestParam("name") String name,
            RedirectAttributes redirectAttributes
    ) {
        try {
            basketCourseAdminService.createBasket(courseId, name);
            redirectAttributes.addFlashAttribute("message", "Basket saved");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/basket-courses/" + courseId;
    }

    @PostMapping("/{courseId}/baskets/{basketId}")
    public String updateBasket(
            @PathVariable Long courseId,
            @PathVariable Long basketId,
            @RequestParam("name") String name,
            RedirectAttributes redirectAttributes
    ) {
        try {
            basketCourseAdminService.updateBasket(courseId, basketId, name);
            redirectAttributes.addFlashAttribute("message", "Basket updated");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/basket-courses/" + courseId;
    }

    @PostMapping("/{courseId}/baskets/{basketId}/variations")
    public String createVariation(
            @PathVariable Long courseId,
            @PathVariable Long basketId,
            @RequestParam("name") String name,
            @RequestParam("distance") String distance,
            RedirectAttributes redirectAttributes
    ) {
        try {
            basketCourseAdminService.createVariation(courseId, basketId, name, distance);
            redirectAttributes.addFlashAttribute("message", "Variation saved");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/basket-courses/" + courseId;
    }

    @PostMapping("/{courseId}/baskets/{basketId}/variations/{variationId}")
    public String updateVariation(
            @PathVariable Long courseId,
            @PathVariable Long basketId,
            @PathVariable Long variationId,
            @RequestParam("name") String name,
            @RequestParam("distance") String distance,
            RedirectAttributes redirectAttributes
    ) {
        try {
            basketCourseAdminService.updateVariation(courseId, basketId, variationId, name, distance);
            redirectAttributes.addFlashAttribute("message", "Variation updated");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/basket-courses/" + courseId;
    }
}
