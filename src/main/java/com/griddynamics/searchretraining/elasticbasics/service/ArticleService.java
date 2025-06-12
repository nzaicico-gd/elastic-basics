package com.griddynamics.searchretraining.elasticbasics.service;

import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchRequest;
import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchResponse;

public interface ArticleService {

    void createIndex();

    ArticleSearchResponse searchArticles(ArticleSearchRequest request);
}
