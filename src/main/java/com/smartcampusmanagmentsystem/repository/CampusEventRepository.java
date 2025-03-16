package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.CampusEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the CampusEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampusEventRepository extends ReactiveMongoRepository<CampusEvent, String> {
    Flux<CampusEvent> findAllBy(Pageable pageable);
}
