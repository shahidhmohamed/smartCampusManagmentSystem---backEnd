package com.smartcampusmanagmentsystem.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.smartcampusmanagmentsystem.domain.ResourceBooking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link ResourceBooking} entity.
 */
public interface ResourceBookingSearchRepository
    extends ReactiveElasticsearchRepository<ResourceBooking, String>, ResourceBookingSearchRepositoryInternal {}

interface ResourceBookingSearchRepositoryInternal {
    Flux<ResourceBooking> search(String query, Pageable pageable);

    Flux<ResourceBooking> search(Query query);
}

class ResourceBookingSearchRepositoryInternalImpl implements ResourceBookingSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    ResourceBookingSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<ResourceBooking> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<ResourceBooking> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, ResourceBooking.class).map(SearchHit::getContent);
    }
}
