package com.smartcampusmanagmentsystem.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.smartcampusmanagmentsystem.domain.GroupChat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link GroupChat} entity.
 */
public interface GroupChatSearchRepository extends ReactiveElasticsearchRepository<GroupChat, String>, GroupChatSearchRepositoryInternal {}

interface GroupChatSearchRepositoryInternal {
    Flux<GroupChat> search(String query, Pageable pageable);

    Flux<GroupChat> search(Query query);
}

class GroupChatSearchRepositoryInternalImpl implements GroupChatSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    GroupChatSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<GroupChat> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<GroupChat> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, GroupChat.class).map(SearchHit::getContent);
    }
}
