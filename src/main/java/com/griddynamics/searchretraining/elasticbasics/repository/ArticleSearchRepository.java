package com.griddynamics.searchretraining.elasticbasics.repository;

import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchRequest;
import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchResponse;

public interface ArticleSearchRepository {

    ArticleSearchResponse searchArticles(ArticleSearchRequest request);

}
