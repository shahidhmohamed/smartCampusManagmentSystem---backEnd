package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.AssignmentFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the AssignmentFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignmentFileRepository extends ReactiveMongoRepository<AssignmentFile, String> {
    Flux<AssignmentFile> findAllBy(Pageable pageable);
}
