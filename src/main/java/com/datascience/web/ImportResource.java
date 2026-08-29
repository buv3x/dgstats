package com.datascience.web;

import com.datascience.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImportResource {

    private final ImportService importService;

    @PostMapping(value = "/import/pdga", produces = MediaType.TEXT_PLAIN_VALUE)
    public String importPdga() {
        importService.importPendingCompetitions();
        return "Import initiated";
    }
}
