package com.smartcampusmanagmentsystem.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.smartcampusmanagmentsystem.domain.CourseRegistration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link CourseRegistration} entity.
 */
public interface CourseRegistrationSearchRepository
    extends ReactiveElasticsearchRepository<CourseRegistration, String>, CourseRegistrationSearchRepositoryInternal {}

interface CourseRegistrationSearchRepositoryInternal {
    Flux<CourseRegistration> search(String query, Pageable pageable);

    Flux<CourseRegistration> search(Query query);
}

class CourseRegistrationSearchRepositoryInternalImpl implements CourseRegistrationSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    CourseRegistrationSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<CourseRegistration> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<CourseRegistration> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, CourseRegistration.class).map(SearchHit::getContent);
    }
}
