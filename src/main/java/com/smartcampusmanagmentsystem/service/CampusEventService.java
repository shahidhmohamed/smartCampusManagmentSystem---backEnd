package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.CampusEventRepository;
import com.smartcampusmanagmentsystem.repository.search.CampusEventSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.CampusEventDTO;
import com.smartcampusmanagmentsystem.service.mapper.CampusEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.CampusEvent}.
 */
@Service
public class CampusEventService {

    private static final Logger LOG = LoggerFactory.getLogger(CampusEventService.class);

    private final CampusEventRepository campusEventRepository;

    private final CampusEventMapper campusEventMapper;

    private final CampusEventSearchRepository campusEventSearchRepository;

    public CampusEventService(
        CampusEventRepository campusEventRepository,
        CampusEventMapper campusEventMapper,
        CampusEventSearchRepository campusEventSearchRepository
    ) {
        this.campusEventRepository = campusEventRepository;
        this.campusEventMapper = campusEventMapper;
        this.campusEventSearchRepository = campusEventSearchRepository;
    }

    /**
     * Save a campusEvent.
     *
     * @param campusEventDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CampusEventDTO> save(CampusEventDTO campusEventDTO) {
        LOG.debug("Request to save CampusEvent : {}", campusEventDTO);
        return campusEventRepository
            .save(campusEventMapper.toEntity(campusEventDTO))
            .flatMap(campusEventSearchRepository::save)
            .map(campusEventMapper::toDto);
    }

    /**
     * Update a campusEvent.
     *
     * @param campusEventDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CampusEventDTO> update(CampusEventDTO campusEventDTO) {
        LOG.debug("Request to update CampusEvent : {}", campusEventDTO);
        return campusEventRepository
            .save(campusEventMapper.toEntity(campusEventDTO))
            .flatMap(campusEventSearchRepository::save)
            .map(campusEventMapper::toDto);
    }

    /**
     * Partially update a campusEvent.
     *
     * @param campusEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CampusEventDTO> partialUpdate(CampusEventDTO campusEventDTO) {
        LOG.debug("Request to partially update CampusEvent : {}", campusEventDTO);

        return campusEventRepository
            .findById(campusEventDTO.getId())
            .map(existingCampusEvent -> {
                campusEventMapper.partialUpdate(existingCampusEvent, campusEventDTO);

                return existingCampusEvent;
            })
            .flatMap(campusEventRepository::save)
            .flatMap(savedCampusEvent -> {
                campusEventSearchRepository.save(savedCampusEvent);
                return Mono.just(savedCampusEvent);
            })
            .map(campusEventMapper::toDto);
    }

    /**
     * Get all the campusEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CampusEventDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CampusEvents");
        return campusEventRepository.findAllBy(pageable).map(campusEventMapper::toDto);
    }

    /**
     * Returns the number of campusEvents available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return campusEventRepository.count();
    }

    /**
     * Returns the number of campusEvents available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return campusEventSearchRepository.count();
    }

    /**
     * Get one campusEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<CampusEventDTO> findOne(String id) {
        LOG.debug("Request to get CampusEvent : {}", id);
        return campusEventRepository.findById(id).map(campusEventMapper::toDto);
    }

    /**
     * Delete the campusEvent by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete CampusEvent : {}", id);
        return campusEventRepository.deleteById(id).then(campusEventSearchRepository.deleteById(id));
    }

    /**
     * Search for the campusEvent corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CampusEventDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of CampusEvents for query {}", query);
        return campusEventSearchRepository.search(query, pageable).map(campusEventMapper::toDto);
    }
}
