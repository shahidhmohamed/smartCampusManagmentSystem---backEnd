package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.AttendenceRepository;
import com.smartcampusmanagmentsystem.service.AttendenceService;
import com.smartcampusmanagmentsystem.service.dto.AttendenceDTO;
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
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.Attendence}.
 */
@RestController
@RequestMapping("/api/attendences")
public class AttendenceResource {

    private static final Logger LOG = LoggerFactory.getLogger(AttendenceResource.class);

    private static final String ENTITY_NAME = "attendence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttendenceService attendenceService;

    private final AttendenceRepository attendenceRepository;

    public AttendenceResource(AttendenceService attendenceService, AttendenceRepository attendenceRepository) {
        this.attendenceService = attendenceService;
        this.attendenceRepository = attendenceRepository;
    }

    /**
     * {@code POST  /attendences} : Create a new attendence.
     *
     * @param attendenceDTO the attendenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attendenceDTO, or with status {@code 400 (Bad Request)} if the attendence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AttendenceDTO>> createAttendence(@RequestBody AttendenceDTO attendenceDTO) throws URISyntaxException {
        LOG.debug("REST request to save Attendence : {}", attendenceDTO);
        if (attendenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new attendence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return attendenceService
            .save(attendenceDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/attendences/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /attendences/:id} : Updates an existing attendence.
     *
     * @param id the id of the attendenceDTO to save.
     * @param attendenceDTO the attendenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendenceDTO,
     * or with status {@code 400 (Bad Request)} if the attendenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attendenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AttendenceDTO>> updateAttendence(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttendenceDTO attendenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Attendence : {}, {}", id, attendenceDTO);
        if (attendenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attendenceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return attendenceService
                    .update(attendenceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /attendences/:id} : Partial updates given fields of an existing attendence, field will ignore if it is null
     *
     * @param id the id of the attendenceDTO to save.
     * @param attendenceDTO the attendenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendenceDTO,
     * or with status {@code 400 (Bad Request)} if the attendenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the attendenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the attendenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AttendenceDTO>> partialUpdateAttendence(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttendenceDTO attendenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Attendence partially : {}, {}", id, attendenceDTO);
        if (attendenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attendenceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AttendenceDTO> result = attendenceService.partialUpdate(attendenceDTO);

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
     * {@code GET  /attendences} : get all the attendences.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attendences in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AttendenceDTO>>> getAllAttendences(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Attendences");
        return attendenceService
            .countAll()
            .zipWith(attendenceService.findAll(pageable).collectList())
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
     * {@code GET  /attendences/:id} : get the "id" attendence.
     *
     * @param id the id of the attendenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attendenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AttendenceDTO>> getAttendence(@PathVariable("id") String id) {
        LOG.debug("REST request to get Attendence : {}", id);
        Mono<AttendenceDTO> attendenceDTO = attendenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attendenceDTO);
    }

    /**
     * {@code DELETE  /attendences/:id} : delete the "id" attendence.
     *
     * @param id the id of the attendenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAttendence(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Attendence : {}", id);
        return attendenceService
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
     * {@code SEARCH  /attendences/_search?query=:query} : search for the attendence corresponding
     * to the query.
     *
     * @param query the query of the attendence search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<AttendenceDTO>>> searchAttendences(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of Attendences for query {}", query);
        return attendenceService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(attendenceService.search(query, pageable)));
    }
}
