package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.File;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the File entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileRepository extends ReactiveMongoRepository<File, String> {
    Flux<File> findAllBy(Pageable pageable);
}
