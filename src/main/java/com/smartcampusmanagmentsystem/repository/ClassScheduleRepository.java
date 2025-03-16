package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.ClassSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ClassSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassScheduleRepository extends ReactiveMongoRepository<ClassSchedule, String> {
    Flux<ClassSchedule> findAllBy(Pageable pageable);
}
