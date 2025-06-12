package com.griddynamics.searchretraining.elasticbasics.model;

import java.util.Optional;

public record ArticleSearchRequest(
        String textQuery,
        Optional<String> authorFilter,
        Optional<String> genreFilter
) {
}
