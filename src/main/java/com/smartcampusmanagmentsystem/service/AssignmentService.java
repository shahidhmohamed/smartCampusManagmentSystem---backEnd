package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.AssignmentRepository;
import com.smartcampusmanagmentsystem.repository.search.AssignmentSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.AssignmentDTO;
import com.smartcampusmanagmentsystem.service.mapper.AssignmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.Assignment}.
 */
@Service
public class AssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AssignmentService.class);

    private final AssignmentRepository assignmentRepository;

    private final AssignmentMapper assignmentMapper;

    private final AssignmentSearchRepository assignmentSearchRepository;

    public AssignmentService(
        AssignmentRepository assignmentRepository,
        AssignmentMapper assignmentMapper,
        AssignmentSearchRepository assignmentSearchRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.assignmentSearchRepository = assignmentSearchRepository;
    }

    /**
     * Save a assignment.
     *
     * @param assignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssignmentDTO> save(AssignmentDTO assignmentDTO) {
        LOG.debug("Request to save Assignment : {}", assignmentDTO);
        return assignmentRepository
            .save(assignmentMapper.toEntity(assignmentDTO))
            .flatMap(assignmentSearchRepository::save)
            .map(assignmentMapper::toDto);
    }

    /**
     * Update a assignment.
     *
     * @param assignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssignmentDTO> update(AssignmentDTO assignmentDTO) {
        LOG.debug("Request to update Assignment : {}", assignmentDTO);
        return assignmentRepository
            .save(assignmentMapper.toEntity(assignmentDTO))
            .flatMap(assignmentSearchRepository::save)
            .map(assignmentMapper::toDto);
    }

    /**
     * Partially update a assignment.
     *
     * @param assignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AssignmentDTO> partialUpdate(AssignmentDTO assignmentDTO) {
        LOG.debug("Request to partially update Assignment : {}", assignmentDTO);

        return assignmentRepository
            .findById(assignmentDTO.getId())
            .map(existingAssignment -> {
                assignmentMapper.partialUpdate(existingAssignment, assignmentDTO);

                return existingAssignment;
            })
            .flatMap(assignmentRepository::save)
            .flatMap(savedAssignment -> {
                assignmentSearchRepository.save(savedAssignment);
                return Mono.just(savedAssignment);
            })
            .map(assignmentMapper::toDto);
    }

    /**
     * Get all the assignments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AssignmentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Assignments");
        return assignmentRepository.findAllBy(pageable).map(assignmentMapper::toDto);
    }

    /**
     * Returns the number of assignments available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return assignmentRepository.count();
    }

    /**
     * Returns the number of assignments available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return assignmentSearchRepository.count();
    }

    /**
     * Get one assignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<AssignmentDTO> findOne(String id) {
        LOG.debug("Request to get Assignment : {}", id);
        return assignmentRepository.findById(id).map(assignmentMapper::toDto);
    }

    /**
     * Delete the assignment by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Assignment : {}", id);
        return assignmentRepository.deleteById(id).then(assignmentSearchRepository.deleteById(id));
    }

    /**
     * Search for the assignment corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AssignmentDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Assignments for query {}", query);
        return assignmentSearchRepository.search(query, pageable).map(assignmentMapper::toDto);
    }
}
