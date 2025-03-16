package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.ClassScheduleRepository;
import com.smartcampusmanagmentsystem.repository.search.ClassScheduleSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ClassScheduleDTO;
import com.smartcampusmanagmentsystem.service.mapper.ClassScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.ClassSchedule}.
 */
@Service
public class ClassScheduleService {

    private static final Logger LOG = LoggerFactory.getLogger(ClassScheduleService.class);

    private final ClassScheduleRepository classScheduleRepository;

    private final ClassScheduleMapper classScheduleMapper;

    private final ClassScheduleSearchRepository classScheduleSearchRepository;

    public ClassScheduleService(
        ClassScheduleRepository classScheduleRepository,
        ClassScheduleMapper classScheduleMapper,
        ClassScheduleSearchRepository classScheduleSearchRepository
    ) {
        this.classScheduleRepository = classScheduleRepository;
        this.classScheduleMapper = classScheduleMapper;
        this.classScheduleSearchRepository = classScheduleSearchRepository;
    }

    /**
     * Save a classSchedule.
     *
     * @param classScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClassScheduleDTO> save(ClassScheduleDTO classScheduleDTO) {
        LOG.debug("Request to save ClassSchedule : {}", classScheduleDTO);
        return classScheduleRepository
            .save(classScheduleMapper.toEntity(classScheduleDTO))
            .flatMap(classScheduleSearchRepository::save)
            .map(classScheduleMapper::toDto);
    }

    /**
     * Update a classSchedule.
     *
     * @param classScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClassScheduleDTO> update(ClassScheduleDTO classScheduleDTO) {
        LOG.debug("Request to update ClassSchedule : {}", classScheduleDTO);
        return classScheduleRepository
            .save(classScheduleMapper.toEntity(classScheduleDTO))
            .flatMap(classScheduleSearchRepository::save)
            .map(classScheduleMapper::toDto);
    }

    /**
     * Partially update a classSchedule.
     *
     * @param classScheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ClassScheduleDTO> partialUpdate(ClassScheduleDTO classScheduleDTO) {
        LOG.debug("Request to partially update ClassSchedule : {}", classScheduleDTO);

        return classScheduleRepository
            .findById(classScheduleDTO.getId())
            .map(existingClassSchedule -> {
                classScheduleMapper.partialUpdate(existingClassSchedule, classScheduleDTO);

                return existingClassSchedule;
            })
            .flatMap(classScheduleRepository::save)
            .flatMap(savedClassSchedule -> {
                classScheduleSearchRepository.save(savedClassSchedule);
                return Mono.just(savedClassSchedule);
            })
            .map(classScheduleMapper::toDto);
    }

    /**
     * Get all the classSchedules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ClassScheduleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ClassSchedules");
        return classScheduleRepository.findAllBy(pageable).map(classScheduleMapper::toDto);
    }

    /**
     * Returns the number of classSchedules available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return classScheduleRepository.count();
    }

    /**
     * Returns the number of classSchedules available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return classScheduleSearchRepository.count();
    }

    /**
     * Get one classSchedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ClassScheduleDTO> findOne(String id) {
        LOG.debug("Request to get ClassSchedule : {}", id);
        return classScheduleRepository.findById(id).map(classScheduleMapper::toDto);
    }

    /**
     * Delete the classSchedule by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete ClassSchedule : {}", id);
        return classScheduleRepository.deleteById(id).then(classScheduleSearchRepository.deleteById(id));
    }

    /**
     * Search for the classSchedule corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ClassScheduleDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ClassSchedules for query {}", query);
        return classScheduleSearchRepository.search(query, pageable).map(classScheduleMapper::toDto);
    }
}
