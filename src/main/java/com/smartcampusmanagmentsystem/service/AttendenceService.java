package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.AttendenceRepository;
import com.smartcampusmanagmentsystem.repository.search.AttendenceSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.AttendenceDTO;
import com.smartcampusmanagmentsystem.service.mapper.AttendenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.Attendence}.
 */
@Service
public class AttendenceService {

    private static final Logger LOG = LoggerFactory.getLogger(AttendenceService.class);

    private final AttendenceRepository attendenceRepository;

    private final AttendenceMapper attendenceMapper;

    private final AttendenceSearchRepository attendenceSearchRepository;

    public AttendenceService(
        AttendenceRepository attendenceRepository,
        AttendenceMapper attendenceMapper,
        AttendenceSearchRepository attendenceSearchRepository
    ) {
        this.attendenceRepository = attendenceRepository;
        this.attendenceMapper = attendenceMapper;
        this.attendenceSearchRepository = attendenceSearchRepository;
    }

    /**
     * Save a attendence.
     *
     * @param attendenceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AttendenceDTO> save(AttendenceDTO attendenceDTO) {
        LOG.debug("Request to save Attendence : {}", attendenceDTO);
        return attendenceRepository
            .save(attendenceMapper.toEntity(attendenceDTO))
            .flatMap(attendenceSearchRepository::save)
            .map(attendenceMapper::toDto);
    }

    /**
     * Update a attendence.
     *
     * @param attendenceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AttendenceDTO> update(AttendenceDTO attendenceDTO) {
        LOG.debug("Request to update Attendence : {}", attendenceDTO);
        return attendenceRepository
            .save(attendenceMapper.toEntity(attendenceDTO))
            .flatMap(attendenceSearchRepository::save)
            .map(attendenceMapper::toDto);
    }

    /**
     * Partially update a attendence.
     *
     * @param attendenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AttendenceDTO> partialUpdate(AttendenceDTO attendenceDTO) {
        LOG.debug("Request to partially update Attendence : {}", attendenceDTO);

        return attendenceRepository
            .findById(attendenceDTO.getId())
            .map(existingAttendence -> {
                attendenceMapper.partialUpdate(existingAttendence, attendenceDTO);

                return existingAttendence;
            })
            .flatMap(attendenceRepository::save)
            .flatMap(savedAttendence -> {
                attendenceSearchRepository.save(savedAttendence);
                return Mono.just(savedAttendence);
            })
            .map(attendenceMapper::toDto);
    }

    /**
     * Get all the attendences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AttendenceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Attendences");
        return attendenceRepository.findAllBy(pageable).map(attendenceMapper::toDto);
    }

    /**
     * Returns the number of attendences available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return attendenceRepository.count();
    }

    /**
     * Returns the number of attendences available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return attendenceSearchRepository.count();
    }

    /**
     * Get one attendence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<AttendenceDTO> findOne(String id) {
        LOG.debug("Request to get Attendence : {}", id);
        return attendenceRepository.findById(id).map(attendenceMapper::toDto);
    }

    /**
     * Delete the attendence by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Attendence : {}", id);
        return attendenceRepository.deleteById(id).then(attendenceSearchRepository.deleteById(id));
    }

    /**
     * Search for the attendence corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AttendenceDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Attendences for query {}", query);
        return attendenceSearchRepository.search(query, pageable).map(attendenceMapper::toDto);
    }
}
