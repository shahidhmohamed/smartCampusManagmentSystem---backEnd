package com.smartcampusmanagmentsystem.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link AttendenceStudentsRecord} entity.
 */
public interface AttendenceStudentsRecordSearchRepository
    extends ReactiveElasticsearchRepository<AttendenceStudentsRecord, String>, AttendenceStudentsRecordSearchRepositoryInternal {}

interface AttendenceStudentsRecordSearchRepositoryInternal {
    Flux<AttendenceStudentsRecord> search(String query, Pageable pageable);

    Flux<AttendenceStudentsRecord> search(Query query);
}

class AttendenceStudentsRecordSearchRepositoryInternalImpl implements AttendenceStudentsRecordSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    AttendenceStudentsRecordSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<AttendenceStudentsRecord> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<AttendenceStudentsRecord> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, AttendenceStudentsRecord.class).map(SearchHit::getContent);
    }
}
