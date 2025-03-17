package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.GroupChatMembers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the GroupChatMembers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupChatMembersRepository extends ReactiveMongoRepository<GroupChatMembers, String> {
    Flux<GroupChatMembers> findAllBy(Pageable pageable);
}
