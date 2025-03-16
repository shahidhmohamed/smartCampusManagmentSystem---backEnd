package com.smartcampusmanagmentsystem.repository;

import com.smartcampusmanagmentsystem.domain.ResourceBooking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ResourceBooking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceBookingRepository extends ReactiveMongoRepository<ResourceBooking, String> {
    Flux<ResourceBooking> findAllBy(Pageable pageable);
}
