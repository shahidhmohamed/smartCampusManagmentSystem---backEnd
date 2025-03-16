package com.smartcampusmanagmentsystem.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.smartcampusmanagmentsystem.domain.AssignmentFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link AssignmentFile} entity.
 */
public interface AssignmentFileSearchRepository
    extends ReactiveElasticsearchRepository<AssignmentFile, String>, AssignmentFileSearchRepositoryInternal {}

interface AssignmentFileSearchRepositoryInternal {
    Flux<AssignmentFile> search(String query, Pageable pageable);

    Flux<AssignmentFile> search(Query query);
}

class AssignmentFileSearchRepositoryInternalImpl implements AssignmentFileSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    AssignmentFileSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<AssignmentFile> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<AssignmentFile> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, AssignmentFile.class).map(SearchHit::getContent);
    }
}
