package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.ChatUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ChatUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatUserRepository extends ReactiveMongoRepository<ChatUser, String> {
    Flux<ChatUser> findAllBy(Pageable pageable);
}
