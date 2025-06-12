package com.griddynamics.searchretraining.elasticbasics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ArticleResponse(
        String title,
        String author,
        String description,
        List<String> genres
) {
}
