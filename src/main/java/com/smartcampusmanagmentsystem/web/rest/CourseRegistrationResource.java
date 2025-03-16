package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.CourseRegistrationRepository;
import com.smartcampusmanagmentsystem.service.CourseRegistrationService;
import com.smartcampusmanagmentsystem.service.dto.CourseRegistrationDTO;
import com.smartcampusmanagmentsystem.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.CourseRegistration}.
 */
@RestController
@RequestMapping("/api/course-registrations")
public class CourseRegistrationResource {

    private static final Logger LOG = LoggerFactory.getLogger(CourseRegistrationResource.class);

    private static final String ENTITY_NAME = "courseRegistration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseRegistrationService courseRegistrationService;

    private final CourseRegistrationRepository courseRegistrationRepository;

    public CourseRegistrationResource(
        CourseRegistrationService courseRegistrationService,
        CourseRegistrationRepository courseRegistrationRepository
    ) {
        this.courseRegistrationService = courseRegistrationService;
        this.courseRegistrationRepository = courseRegistrationRepository;
    }

    /**
     * {@code POST  /course-registrations} : Create a new courseRegistration.
     *
     * @param courseRegistrationDTO the courseRegistrationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseRegistrationDTO, or with status {@code 400 (Bad Request)} if the courseRegistration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CourseRegistrationDTO>> createCourseRegistration(@RequestBody CourseRegistrationDTO courseRegistrationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CourseRegistration : {}", courseRegistrationDTO);
        if (courseRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return courseRegistrationService
            .save(courseRegistrationDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/course-registrations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /course-registrations/:id} : Updates an existing courseRegistration.
     *
     * @param id the id of the courseRegistrationDTO to save.
     * @param courseRegistrationDTO the courseRegistrationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseRegistrationDTO,
     * or with status {@code 400 (Bad Request)} if the courseRegistrationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseRegistrationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CourseRegistrationDTO>> updateCourseRegistration(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CourseRegistrationDTO courseRegistrationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CourseRegistration : {}, {}", id, courseRegistrationDTO);
        if (courseRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseRegistrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return courseRegistrationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return courseRegistrationService
                    .update(courseRegistrationDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /course-registrations/:id} : Partial updates given fields of an existing courseRegistration, field will ignore if it is null
     *
     * @param id the id of the courseRegistrationDTO to save.
     * @param courseRegistrationDTO the courseRegistrationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseRegistrationDTO,
     * or with status {@code 400 (Bad Request)} if the courseRegistrationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the courseRegistrationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseRegistrationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CourseRegistrationDTO>> partialUpdateCourseRegistration(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CourseRegistrationDTO courseRegistrationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CourseRegistration partially : {}, {}", id, courseRegistrationDTO);
        if (courseRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseRegistrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return courseRegistrationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CourseRegistrationDTO> result = courseRegistrationService.partialUpdate(courseRegistrationDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /course-registrations} : get all the courseRegistrations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseRegistrations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CourseRegistrationDTO>>> getAllCourseRegistrations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of CourseRegistrations");
        return courseRegistrationService
            .countAll()
            .zipWith(courseRegistrationService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /course-registrations/:id} : get the "id" courseRegistration.
     *
     * @param id the id of the courseRegistrationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseRegistrationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CourseRegistrationDTO>> getCourseRegistration(@PathVariable("id") String id) {
        LOG.debug("REST request to get CourseRegistration : {}", id);
        Mono<CourseRegistrationDTO> courseRegistrationDTO = courseRegistrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseRegistrationDTO);
    }

    /**
     * {@code DELETE  /course-registrations/:id} : delete the "id" courseRegistration.
     *
     * @param id the id of the courseRegistrationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCourseRegistration(@PathVariable("id") String id) {
        LOG.debug("REST request to delete CourseRegistration : {}", id);
        return courseRegistrationService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                        .build()
                )
            );
    }

    /**
     * {@code SEARCH  /course-registrations/_search?query=:query} : search for the courseRegistration corresponding
     * to the query.
     *
     * @param query the query of the courseRegistration search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<CourseRegistrationDTO>>> searchCourseRegistrations(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of CourseRegistrations for query {}", query);
        return courseRegistrationService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(courseRegistrationService.search(query, pageable)));
    }
}
