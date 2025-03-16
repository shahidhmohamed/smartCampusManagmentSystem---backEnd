package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.ClassScheduleRepository;
import com.smartcampusmanagmentsystem.service.ClassScheduleService;
import com.smartcampusmanagmentsystem.service.dto.ClassScheduleDTO;
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
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.ClassSchedule}.
 */
@RestController
@RequestMapping("/api/class-schedules")
public class ClassScheduleResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClassScheduleResource.class);

    private static final String ENTITY_NAME = "classSchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassScheduleService classScheduleService;

    private final ClassScheduleRepository classScheduleRepository;

    public ClassScheduleResource(ClassScheduleService classScheduleService, ClassScheduleRepository classScheduleRepository) {
        this.classScheduleService = classScheduleService;
        this.classScheduleRepository = classScheduleRepository;
    }

    /**
     * {@code POST  /class-schedules} : Create a new classSchedule.
     *
     * @param classScheduleDTO the classScheduleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classScheduleDTO, or with status {@code 400 (Bad Request)} if the classSchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ClassScheduleDTO>> createClassSchedule(@RequestBody ClassScheduleDTO classScheduleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ClassSchedule : {}", classScheduleDTO);
        if (classScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new classSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return classScheduleService
            .save(classScheduleDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/class-schedules/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /class-schedules/:id} : Updates an existing classSchedule.
     *
     * @param id the id of the classScheduleDTO to save.
     * @param classScheduleDTO the classScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the classScheduleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ClassScheduleDTO>> updateClassSchedule(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ClassScheduleDTO classScheduleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClassSchedule : {}, {}", id, classScheduleDTO);
        if (classScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classScheduleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return classScheduleService
                    .update(classScheduleDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /class-schedules/:id} : Partial updates given fields of an existing classSchedule, field will ignore if it is null
     *
     * @param id the id of the classScheduleDTO to save.
     * @param classScheduleDTO the classScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the classScheduleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the classScheduleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the classScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ClassScheduleDTO>> partialUpdateClassSchedule(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ClassScheduleDTO classScheduleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ClassSchedule partially : {}, {}", id, classScheduleDTO);
        if (classScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classScheduleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ClassScheduleDTO> result = classScheduleService.partialUpdate(classScheduleDTO);

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
     * {@code GET  /class-schedules} : get all the classSchedules.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classSchedules in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ClassScheduleDTO>>> getAllClassSchedules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of ClassSchedules");
        return classScheduleService
            .countAll()
            .zipWith(classScheduleService.findAll(pageable).collectList())
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
     * {@code GET  /class-schedules/:id} : get the "id" classSchedule.
     *
     * @param id the id of the classScheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classScheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ClassScheduleDTO>> getClassSchedule(@PathVariable("id") String id) {
        LOG.debug("REST request to get ClassSchedule : {}", id);
        Mono<ClassScheduleDTO> classScheduleDTO = classScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classScheduleDTO);
    }

    /**
     * {@code DELETE  /class-schedules/:id} : delete the "id" classSchedule.
     *
     * @param id the id of the classScheduleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteClassSchedule(@PathVariable("id") String id) {
        LOG.debug("REST request to delete ClassSchedule : {}", id);
        return classScheduleService
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
     * {@code SEARCH  /class-schedules/_search?query=:query} : search for the classSchedule corresponding
     * to the query.
     *
     * @param query the query of the classSchedule search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<ClassScheduleDTO>>> searchClassSchedules(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of ClassSchedules for query {}", query);
        return classScheduleService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(classScheduleService.search(query, pageable)));
    }
}
