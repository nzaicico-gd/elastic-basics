package com.griddynamics.searchretraining.elasticbasics.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.util.NamedValue;
import com.griddynamics.searchretraining.elasticbasics.config.IndexProperties;
import com.griddynamics.searchretraining.elasticbasics.model.ArticleResponse;
import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchRequest;
import com.griddynamics.searchretraining.elasticbasics.model.ArticleSearchResponse;
import com.griddynamics.searchretraining.elasticbasics.model.FacetMetric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleSearchRepositoryImpl implements ArticleSearchRepository {

    private final ElasticsearchClient esClient;

    private final IndexProperties indexProperties;

    @Override
    public ArticleSearchResponse searchArticles(ArticleSearchRequest request) {
        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder()
                .index(indexProperties.alias())
                .size(10);

        searchRequestBuilder.query(q -> q
                .bool(mainBool -> {

                    if (request.textQuery() != null && !request.textQuery().isBlank()) {
                        mainBool.must(m -> m
                                .multiMatch(mm -> mm
                                        .query(request.textQuery())
                                        .fields("title", "description")
                                        .type(TextQueryType.CrossFields)
                                        .operator(Operator.And)));
                    } else {
                        mainBool.must(m -> m
                                .matchAll(ma -> ma));
                    }

                    if (request.authorFilter().isPresent()) {
                        mainBool.filter(f -> f
                                .term(t -> t
                                        .field("author")
                                        .value(request.authorFilter().get())));
                    }

                    if (request.genreFilter().isPresent()) {
                        mainBool.filter(f -> f
                                .term(t -> t
                                        .field("genres")
                                        .value(request.genreFilter().get())));
                    }

                    return mainBool;
                }));

        searchRequestBuilder.aggregations("genres", a -> a
                .terms(t -> t
                        .field("genres.raw")
                        .order(List.of(
                                new NamedValue<>("_count", SortOrder.Desc)
                        ))
                        .size(5)
                )
        );

        searchRequestBuilder.aggregations("authors", a -> a
                .terms(t -> t
                        .field("author.raw")
                        .order(List.of(
                                new NamedValue<>("_count", SortOrder.Desc)
                        ))
                        .size(5)
                )
        );
        try {
            SearchResponse<ArticleResponse> response = esClient.search(searchRequestBuilder.build(), ArticleResponse.class);
            return getServiceResponse(response);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private ArticleSearchResponse getServiceResponse(SearchResponse<ArticleResponse> response) {
        long totalHits = response.hits().total() != null ? response.hits().total().value() : 0L;

        List<ArticleResponse> articles = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

        Map<String, List<FacetMetric>> facets = extractFacets(response);

        return new ArticleSearchResponse(totalHits, articles, facets);
    }

    private Map<String, List<FacetMetric>> extractFacets(SearchResponse<ArticleResponse> response) {
        Map<String, List<FacetMetric>> facets = new HashMap<>();
        Map<String, Aggregate> aggregations = response.aggregations();

        Optional.ofNullable(aggregations.get("genres"))
                .ifPresent(agg -> facets.put("genres", mapToFacetMetrics(agg)));

        Optional.ofNullable(aggregations.get("authors"))
                .ifPresent(agg -> facets.put("authors", mapToFacetMetrics(agg)));

        return facets;
    }

    private List<FacetMetric> mapToFacetMetrics(Aggregate aggregate) {
        if (aggregate.isSterms()) {
            return aggregate.sterms().buckets().array().stream()
                    .map(bucket -> new FacetMetric(bucket.key().stringValue(), bucket.docCount()))
                    .toList();
        }

        return List.of();
    }
}
