package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.Attendence;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Attendence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttendenceRepository extends ReactiveMongoRepository<Attendence, String> {
    Flux<Attendence> findAllBy(Pageable pageable);
}
