package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.CourseRegistration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the CourseRegistration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRegistrationRepository extends ReactiveMongoRepository<CourseRegistration, String> {
    Flux<CourseRegistration> findAllBy(Pageable pageable);
}
