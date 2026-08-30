package com.datascience.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/basket-courses";
    }
}
