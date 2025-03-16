package com.smartcampusmanagmentsystem.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.smartcampusmanagmentsystem.domain.CampusEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link CampusEvent} entity.
 */
public interface CampusEventSearchRepository
    extends ReactiveElasticsearchRepository<CampusEvent, String>, CampusEventSearchRepositoryInternal {}

interface CampusEventSearchRepositoryInternal {
    Flux<CampusEvent> search(String query, Pageable pageable);

    Flux<CampusEvent> search(Query query);
}

class CampusEventSearchRepositoryInternalImpl implements CampusEventSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    CampusEventSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<CampusEvent> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<CampusEvent> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, CampusEvent.class).map(SearchHit::getContent);
    }
}
