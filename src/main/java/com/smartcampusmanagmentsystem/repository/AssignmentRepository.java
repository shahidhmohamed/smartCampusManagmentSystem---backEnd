package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Assignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignmentRepository extends ReactiveMongoRepository<Assignment, String> {
    Flux<Assignment> findAllBy(Pageable pageable);
}
