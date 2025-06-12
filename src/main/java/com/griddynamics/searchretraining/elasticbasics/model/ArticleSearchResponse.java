package com.griddynamics.searchretraining.elasticbasics.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArticleSearchResponse(
        Long totalHits,
        List<ArticleResponse> articles,
        Map<String, List<FacetMetric>> facets
) {
}
