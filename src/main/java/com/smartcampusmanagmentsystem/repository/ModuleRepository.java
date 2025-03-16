package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.Module;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Module entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModuleRepository extends ReactiveMongoRepository<Module, String> {
    Flux<Module> findAllBy(Pageable pageable);
}
