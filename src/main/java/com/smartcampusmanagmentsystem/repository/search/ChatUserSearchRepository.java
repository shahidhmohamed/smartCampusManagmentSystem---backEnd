package com.smartcampusmanagmentsystem.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.smartcampusmanagmentsystem.domain.ChatUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link ChatUser} entity.
 */
public interface ChatUserSearchRepository extends ReactiveElasticsearchRepository<ChatUser, String>, ChatUserSearchRepositoryInternal {}

interface ChatUserSearchRepositoryInternal {
    Flux<ChatUser> search(String query, Pageable pageable);

    Flux<ChatUser> search(Query query);
}

class ChatUserSearchRepositoryInternalImpl implements ChatUserSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    ChatUserSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<ChatUser> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<ChatUser> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, ChatUser.class).map(SearchHit::getContent);
    }
}
