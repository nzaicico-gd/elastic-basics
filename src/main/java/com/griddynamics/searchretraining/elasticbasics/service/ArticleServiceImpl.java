package com.griddynamics.searchretraining.elasticbasics.service;

import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchRequest;
import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchResponse;
import com.griddynamics.searchretraining.elasticbasics.repository.ArticleIndexerRepository;
import com.griddynamics.searchretraining.elasticbasics.repository.ArticleSearchRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleSearchRepositoryImpl articleSearchRepository;
    private final ArticleIndexerRepository articleIndexerRepository;

    @Override
    public void createIndex() {
        articleIndexerRepository.createIndex();
    }

    @Override
    public ArticleSearchResponse searchArticles(ArticleSearchRequest request) {
        return articleSearchRepository.searchArticles(request);
    }
}
