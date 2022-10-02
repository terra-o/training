package org.terraO.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.terraO.model.Article;
import org.terraO.service.ResearchService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@AllArgsConstructor
@RestController
@RequestMapping("core")
public class CoreController {
    private final ResearchService researchService;

    @RequestMapping(method = GET, value = "/search", produces = "application/json")
    public ResponseEntity<List<Article>> getArticles()
    {
        return ResponseEntity.ok(researchService.search());
    }
}
