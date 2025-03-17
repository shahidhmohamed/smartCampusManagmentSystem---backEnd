package com.smartcampusmanagmentsystem.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.smartcampusmanagmentsystem.domain.GroupChatMembers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link GroupChatMembers} entity.
 */
public interface GroupChatMembersSearchRepository
    extends ReactiveElasticsearchRepository<GroupChatMembers, String>, GroupChatMembersSearchRepositoryInternal {}

interface GroupChatMembersSearchRepositoryInternal {
    Flux<GroupChatMembers> search(String query, Pageable pageable);

    Flux<GroupChatMembers> search(Query query);
}

class GroupChatMembersSearchRepositoryInternalImpl implements GroupChatMembersSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    GroupChatMembersSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<GroupChatMembers> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<GroupChatMembers> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, GroupChatMembers.class).map(SearchHit::getContent);
    }
}
