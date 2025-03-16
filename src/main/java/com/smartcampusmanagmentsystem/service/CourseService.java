package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.CourseRepository;
import com.smartcampusmanagmentsystem.repository.search.CourseSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.CourseDTO;
import com.smartcampusmanagmentsystem.service.mapper.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.Course}.
 */
@Service
public class CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final CourseSearchRepository courseSearchRepository;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, CourseSearchRepository courseSearchRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.courseSearchRepository = courseSearchRepository;
    }

    /**
     * Save a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CourseDTO> save(CourseDTO courseDTO) {
        LOG.debug("Request to save Course : {}", courseDTO);
        return courseRepository.save(courseMapper.toEntity(courseDTO)).flatMap(courseSearchRepository::save).map(courseMapper::toDto);
    }

    /**
     * Update a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CourseDTO> update(CourseDTO courseDTO) {
        LOG.debug("Request to update Course : {}", courseDTO);
        return courseRepository.save(courseMapper.toEntity(courseDTO)).flatMap(courseSearchRepository::save).map(courseMapper::toDto);
    }

    /**
     * Partially update a course.
     *
     * @param courseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CourseDTO> partialUpdate(CourseDTO courseDTO) {
        LOG.debug("Request to partially update Course : {}", courseDTO);

        return courseRepository
            .findById(courseDTO.getId())
            .map(existingCourse -> {
                courseMapper.partialUpdate(existingCourse, courseDTO);

                return existingCourse;
            })
            .flatMap(courseRepository::save)
            .flatMap(savedCourse -> {
                courseSearchRepository.save(savedCourse);
                return Mono.just(savedCourse);
            })
            .map(courseMapper::toDto);
    }

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CourseDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Courses");
        return courseRepository.findAllBy(pageable).map(courseMapper::toDto);
    }

    /**
     * Returns the number of courses available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return courseRepository.count();
    }

    /**
     * Returns the number of courses available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return courseSearchRepository.count();
    }

    /**
     * Get one course by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<CourseDTO> findOne(String id) {
        LOG.debug("Request to get Course : {}", id);
        return courseRepository.findById(id).map(courseMapper::toDto);
    }

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Course : {}", id);
        return courseRepository.deleteById(id).then(courseSearchRepository.deleteById(id));
    }

    /**
     * Search for the course corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<CourseDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Courses for query {}", query);
        return courseSearchRepository.search(query, pageable).map(courseMapper::toDto);
    }
}
