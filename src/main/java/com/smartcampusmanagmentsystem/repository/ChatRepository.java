package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Chat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
    Flux<Chat> findAllBy(Pageable pageable);
}
