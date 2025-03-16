package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.AssignmentFileRepository;
import com.smartcampusmanagmentsystem.repository.search.AssignmentFileSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.AssignmentFileDTO;
import com.smartcampusmanagmentsystem.service.mapper.AssignmentFileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.AssignmentFile}.
 */
@Service
public class AssignmentFileService {

    private static final Logger LOG = LoggerFactory.getLogger(AssignmentFileService.class);

    private final AssignmentFileRepository assignmentFileRepository;

    private final AssignmentFileMapper assignmentFileMapper;

    private final AssignmentFileSearchRepository assignmentFileSearchRepository;

    public AssignmentFileService(
        AssignmentFileRepository assignmentFileRepository,
        AssignmentFileMapper assignmentFileMapper,
        AssignmentFileSearchRepository assignmentFileSearchRepository
    ) {
        this.assignmentFileRepository = assignmentFileRepository;
        this.assignmentFileMapper = assignmentFileMapper;
        this.assignmentFileSearchRepository = assignmentFileSearchRepository;
    }

    /**
     * Save a assignmentFile.
     *
     * @param assignmentFileDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssignmentFileDTO> save(AssignmentFileDTO assignmentFileDTO) {
        LOG.debug("Request to save AssignmentFile : {}", assignmentFileDTO);
        return assignmentFileRepository
            .save(assignmentFileMapper.toEntity(assignmentFileDTO))
            .flatMap(assignmentFileSearchRepository::save)
            .map(assignmentFileMapper::toDto);
    }

    /**
     * Update a assignmentFile.
     *
     * @param assignmentFileDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssignmentFileDTO> update(AssignmentFileDTO assignmentFileDTO) {
        LOG.debug("Request to update AssignmentFile : {}", assignmentFileDTO);
        return assignmentFileRepository
            .save(assignmentFileMapper.toEntity(assignmentFileDTO))
            .flatMap(assignmentFileSearchRepository::save)
            .map(assignmentFileMapper::toDto);
    }

    /**
     * Partially update a assignmentFile.
     *
     * @param assignmentFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AssignmentFileDTO> partialUpdate(AssignmentFileDTO assignmentFileDTO) {
        LOG.debug("Request to partially update AssignmentFile : {}", assignmentFileDTO);

        return assignmentFileRepository
            .findById(assignmentFileDTO.getId())
            .map(existingAssignmentFile -> {
                assignmentFileMapper.partialUpdate(existingAssignmentFile, assignmentFileDTO);

                return existingAssignmentFile;
            })
            .flatMap(assignmentFileRepository::save)
            .flatMap(savedAssignmentFile -> {
                assignmentFileSearchRepository.save(savedAssignmentFile);
                return Mono.just(savedAssignmentFile);
            })
            .map(assignmentFileMapper::toDto);
    }

    /**
     * Get all the assignmentFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AssignmentFileDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AssignmentFiles");
        return assignmentFileRepository.findAllBy(pageable).map(assignmentFileMapper::toDto);
    }

    /**
     * Returns the number of assignmentFiles available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return assignmentFileRepository.count();
    }

    /**
     * Returns the number of assignmentFiles available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return assignmentFileSearchRepository.count();
    }

    /**
     * Get one assignmentFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<AssignmentFileDTO> findOne(String id) {
        LOG.debug("Request to get AssignmentFile : {}", id);
        return assignmentFileRepository.findById(id).map(assignmentFileMapper::toDto);
    }

    /**
     * Delete the assignmentFile by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete AssignmentFile : {}", id);
        return assignmentFileRepository.deleteById(id).then(assignmentFileSearchRepository.deleteById(id));
    }

    /**
     * Search for the assignmentFile corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AssignmentFileDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AssignmentFiles for query {}", query);
        return assignmentFileSearchRepository.search(query, pageable).map(assignmentFileMapper::toDto);
    }
}
