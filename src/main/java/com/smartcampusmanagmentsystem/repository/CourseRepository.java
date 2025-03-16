package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends ReactiveMongoRepository<Course, String> {
    Flux<Course> findAllBy(Pageable pageable);
}
