package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the AttendenceStudentsRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttendenceStudentsRecordRepository extends ReactiveMongoRepository<AttendenceStudentsRecord, String> {
    Flux<AttendenceStudentsRecord> findAllBy(Pageable pageable);
}
