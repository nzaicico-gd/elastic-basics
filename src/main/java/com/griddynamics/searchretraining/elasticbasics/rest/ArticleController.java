package com.griddynamics.searchretraining.elasticbasics.rest;

import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchRequest;
import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchResponse;
import com.griddynamics.searchretraining.elasticbasics.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/elastic-basics/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ArticleSearchResponse searchArticles(@RequestBody ArticleSearchRequest request) {
        return articleService.searchArticles(request);
    }

    @PostMapping("/index")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createIndex() {
        articleService.createIndex();
    }
}
