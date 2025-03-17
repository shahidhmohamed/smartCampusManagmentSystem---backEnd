package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.GroupChat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the GroupChat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupChatRepository extends ReactiveMongoRepository<GroupChat, String> {
    Flux<GroupChat> findAllBy(Pageable pageable);
}
