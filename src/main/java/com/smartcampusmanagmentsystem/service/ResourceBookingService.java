package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.ResourceBookingRepository;
import com.smartcampusmanagmentsystem.repository.search.ResourceBookingSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ResourceBookingDTO;
import com.smartcampusmanagmentsystem.service.mapper.ResourceBookingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.ResourceBooking}.
 */
@Service
public class ResourceBookingService {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceBookingService.class);

    private final ResourceBookingRepository resourceBookingRepository;

    private final ResourceBookingMapper resourceBookingMapper;

    private final ResourceBookingSearchRepository resourceBookingSearchRepository;

    public ResourceBookingService(
        ResourceBookingRepository resourceBookingRepository,
        ResourceBookingMapper resourceBookingMapper,
        ResourceBookingSearchRepository resourceBookingSearchRepository
    ) {
        this.resourceBookingRepository = resourceBookingRepository;
        this.resourceBookingMapper = resourceBookingMapper;
        this.resourceBookingSearchRepository = resourceBookingSearchRepository;
    }

    /**
     * Save a resourceBooking.
     *
     * @param resourceBookingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResourceBookingDTO> save(ResourceBookingDTO resourceBookingDTO) {
        LOG.debug("Request to save ResourceBooking : {}", resourceBookingDTO);
        return resourceBookingRepository
            .save(resourceBookingMapper.toEntity(resourceBookingDTO))
            .flatMap(resourceBookingSearchRepository::save)
            .map(resourceBookingMapper::toDto);
    }

    /**
     * Update a resourceBooking.
     *
     * @param resourceBookingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResourceBookingDTO> update(ResourceBookingDTO resourceBookingDTO) {
        LOG.debug("Request to update ResourceBooking : {}", resourceBookingDTO);
        return resourceBookingRepository
            .save(resourceBookingMapper.toEntity(resourceBookingDTO))
            .flatMap(resourceBookingSearchRepository::save)
            .map(resourceBookingMapper::toDto);
    }

    /**
     * Partially update a resourceBooking.
     *
     * @param resourceBookingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ResourceBookingDTO> partialUpdate(ResourceBookingDTO resourceBookingDTO) {
        LOG.debug("Request to partially update ResourceBooking : {}", resourceBookingDTO);

        return resourceBookingRepository
            .findById(resourceBookingDTO.getId())
            .map(existingResourceBooking -> {
                resourceBookingMapper.partialUpdate(existingResourceBooking, resourceBookingDTO);

                return existingResourceBooking;
            })
            .flatMap(resourceBookingRepository::save)
            .flatMap(savedResourceBooking -> {
                resourceBookingSearchRepository.save(savedResourceBooking);
                return Mono.just(savedResourceBooking);
            })
            .map(resourceBookingMapper::toDto);
    }

    /**
     * Get all the resourceBookings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ResourceBookingDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ResourceBookings");
        return resourceBookingRepository.findAllBy(pageable).map(resourceBookingMapper::toDto);
    }

    /**
     * Returns the number of resourceBookings available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return resourceBookingRepository.count();
    }

    /**
     * Returns the number of resourceBookings available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return resourceBookingSearchRepository.count();
    }

    /**
     * Get one resourceBooking by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ResourceBookingDTO> findOne(String id) {
        LOG.debug("Request to get ResourceBooking : {}", id);
        return resourceBookingRepository.findById(id).map(resourceBookingMapper::toDto);
    }

    /**
     * Delete the resourceBooking by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete ResourceBooking : {}", id);
        return resourceBookingRepository.deleteById(id).then(resourceBookingSearchRepository.deleteById(id));
    }

    /**
     * Search for the resourceBooking corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ResourceBookingDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ResourceBookings for query {}", query);
        return resourceBookingSearchRepository.search(query, pageable).map(resourceBookingMapper::toDto);
    }
}
