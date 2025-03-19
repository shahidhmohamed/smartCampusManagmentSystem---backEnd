package com.smartcampusmanagmentsystem.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.smartcampusmanagmentsystem.domain.Attendence;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link Attendence} entity.
 */
public interface AttendenceSearchRepository
    extends ReactiveElasticsearchRepository<Attendence, String>, AttendenceSearchRepositoryInternal {}

interface AttendenceSearchRepositoryInternal {
    Flux<Attendence> search(String query, Pageable pageable);

    Flux<Attendence> search(Query query);
}

class AttendenceSearchRepositoryInternalImpl implements AttendenceSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    AttendenceSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Attendence> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<Attendence> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Attendence.class).map(SearchHit::getContent);
    }
}
