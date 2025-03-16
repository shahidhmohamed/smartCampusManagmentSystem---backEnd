package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.CourseRegistrationRepository;
import com.smartcampusmanagmentsystem.repository.search.CourseRegistrationSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.CourseRegistrationDTO;
import com.smartcampusmanagmentsystem.service.mapper.CourseRegistrationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.CourseRegistration}.
 */
@Service
public class CourseRegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseRegistrationService.class);

    private final CourseRegistrationRepository courseRegistrationRepository;

    private final CourseRegistrationMapper courseRegistrationMapper;

    private final CourseRegistrationSearchRepository courseRegistrationSearchRepository;

    public CourseRegistrationService(
        CourseRegistrationRepository courseRegistrationRepository,
        CourseRegistrationMapper courseRegistrationMapper,
        CourseRegistrationSearchRepository courseRegistrationSearchRepository
    ) {
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.courseRegistrationMapper = courseRegistrationMapper;
        this.courseRegistrationSearchRepository = courseRegistrationSearchRepository;
    }

    /**
     * Save a courseRegistration.
     *
     * @param courseRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CourseRegistrationDTO> save(CourseRegistrationDTO courseRegistrationDTO) {
        LOG.debug("Request to save CourseRegistration : {}", courseRegistrationDTO);
        return courseRegistrationRepository
            .save(courseRegistrationMapper.toEntity(courseRegistrationDTO))
            .flatMap(courseRegistrationSearchRepository::save)
            .map(courseRegistrationMapper::toDto);
    }

    /**
     * Update a courseRegistration.
     *
     * @param courseRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CourseRegistrationDTO> update(CourseRegistrationDTO courseRegistrationDTO) {
        LOG.debug("Request to update CourseRegistration : {}", courseRegistrationDTO);
        return courseRegistrationRepository
            .save(courseRegistrationMapper.toEntity(courseRegistrationDTO))
            .flatMap(courseRegistrationSearchRepository::save)
            .map(courseRegistrationMapper::toDto);
    }

    /**
     * Partially update a courseRegistration.
     *
     * @param courseRegistrationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CourseRegistrationDTO> partialUpdate(CourseRegistrationDTO courseRegistrationDTO) {
        LOG.debug("Request to partially update CourseRegistration : {}", courseRegistrationDTO);

        return courseRegistrationRepository
            .findById(courseRegistrationDTO.getId())
            .map(existingCourseRegistration -> {
                courseRegistrationMapper.partialUpdate(existingCourseRegistration, courseRegistrationDTO);

                return existingCourseRegistration;
            })
            .flatMap(courseRegistrationRepository::save)
            .flatMap(savedCourseRegistration -> {
                courseRegistrationSearchRepository.save(savedCourseRegistration);
                return Mono.just(savedCourseRegistration);
            })
            .map(courseRegistrationMapper::toDto);
    }

    /**
     * Get all the courseRegistrations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CourseRegistrationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CourseRegistrations");
        return courseRegistrationRepository.findAllBy(pageable).map(courseRegistrationMapper::toDto);
    }

    /**
     * Returns the number of courseRegistrations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return courseRegistrationRepository.count();
    }

    /**
     * Returns the number of courseRegistrations available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return courseRegistrationSearchRepository.count();
    }

    /**
     * Get one courseRegistration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<CourseRegistrationDTO> findOne(String id) {
        LOG.debug("Request to get CourseRegistration : {}", id);
        return courseRegistrationRepository.findById(id).map(courseRegistrationMapper::toDto);
    }

    /**
     * Delete the courseRegistration by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete CourseRegistration : {}", id);
        return courseRegistrationRepository.deleteById(id).then(courseRegistrationSearchRepository.deleteById(id));
    }

    /**
     * Search for the courseRegistration corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CourseRegistrationDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of CourseRegistrations for query {}", query);
        return courseRegistrationSearchRepository.search(query, pageable).map(courseRegistrationMapper::toDto);
    }
}
