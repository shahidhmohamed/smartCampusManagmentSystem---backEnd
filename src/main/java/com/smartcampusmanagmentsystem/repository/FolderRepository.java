package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.Folder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Folder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FolderRepository extends ReactiveMongoRepository<Folder, String> {
    Flux<Folder> findAllBy(Pageable pageable);
}
