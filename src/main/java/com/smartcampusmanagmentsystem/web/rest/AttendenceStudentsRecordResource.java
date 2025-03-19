package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.AttendenceStudentsRecordRepository;
import com.smartcampusmanagmentsystem.service.AttendenceStudentsRecordService;
import com.smartcampusmanagmentsystem.service.dto.AttendenceStudentsRecordDTO;
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
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecord}.
 */
@RestController
@RequestMapping("/api/attendence-students-records")
public class AttendenceStudentsRecordResource {

    private static final Logger LOG = LoggerFactory.getLogger(AttendenceStudentsRecordResource.class);

    private static final String ENTITY_NAME = "attendenceStudentsRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttendenceStudentsRecordService attendenceStudentsRecordService;

    private final AttendenceStudentsRecordRepository attendenceStudentsRecordRepository;

    public AttendenceStudentsRecordResource(
        AttendenceStudentsRecordService attendenceStudentsRecordService,
        AttendenceStudentsRecordRepository attendenceStudentsRecordRepository
    ) {
        this.attendenceStudentsRecordService = attendenceStudentsRecordService;
        this.attendenceStudentsRecordRepository = attendenceStudentsRecordRepository;
    }

    /**
     * {@code POST  /attendence-students-records} : Create a new attendenceStudentsRecord.
     *
     * @param attendenceStudentsRecordDTO the attendenceStudentsRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attendenceStudentsRecordDTO, or with status {@code 400 (Bad Request)} if the attendenceStudentsRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AttendenceStudentsRecordDTO>> createAttendenceStudentsRecord(
        @RequestBody AttendenceStudentsRecordDTO attendenceStudentsRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AttendenceStudentsRecord : {}", attendenceStudentsRecordDTO);
        if (attendenceStudentsRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new attendenceStudentsRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return attendenceStudentsRecordService
            .save(attendenceStudentsRecordDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/attendence-students-records/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /attendence-students-records/:id} : Updates an existing attendenceStudentsRecord.
     *
     * @param id the id of the attendenceStudentsRecordDTO to save.
     * @param attendenceStudentsRecordDTO the attendenceStudentsRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendenceStudentsRecordDTO,
     * or with status {@code 400 (Bad Request)} if the attendenceStudentsRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attendenceStudentsRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AttendenceStudentsRecordDTO>> updateAttendenceStudentsRecord(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttendenceStudentsRecordDTO attendenceStudentsRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AttendenceStudentsRecord : {}, {}", id, attendenceStudentsRecordDTO);
        if (attendenceStudentsRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendenceStudentsRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attendenceStudentsRecordRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return attendenceStudentsRecordService
                    .update(attendenceStudentsRecordDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /attendence-students-records/:id} : Partial updates given fields of an existing attendenceStudentsRecord, field will ignore if it is null
     *
     * @param id the id of the attendenceStudentsRecordDTO to save.
     * @param attendenceStudentsRecordDTO the attendenceStudentsRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendenceStudentsRecordDTO,
     * or with status {@code 400 (Bad Request)} if the attendenceStudentsRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the attendenceStudentsRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the attendenceStudentsRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AttendenceStudentsRecordDTO>> partialUpdateAttendenceStudentsRecord(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttendenceStudentsRecordDTO attendenceStudentsRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AttendenceStudentsRecord partially : {}, {}", id, attendenceStudentsRecordDTO);
        if (attendenceStudentsRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendenceStudentsRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attendenceStudentsRecordRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AttendenceStudentsRecordDTO> result = attendenceStudentsRecordService.partialUpdate(attendenceStudentsRecordDTO);

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
     * {@code GET  /attendence-students-records} : get all the attendenceStudentsRecords.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attendenceStudentsRecords in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AttendenceStudentsRecordDTO>>> getAllAttendenceStudentsRecords(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of AttendenceStudentsRecords");
        return attendenceStudentsRecordService
            .countAll()
            .zipWith(attendenceStudentsRecordService.findAll(pageable).collectList())
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
     * {@code GET  /attendence-students-records/:id} : get the "id" attendenceStudentsRecord.
     *
     * @param id the id of the attendenceStudentsRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attendenceStudentsRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AttendenceStudentsRecordDTO>> getAttendenceStudentsRecord(@PathVariable("id") String id) {
        LOG.debug("REST request to get AttendenceStudentsRecord : {}", id);
        Mono<AttendenceStudentsRecordDTO> attendenceStudentsRecordDTO = attendenceStudentsRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attendenceStudentsRecordDTO);
    }

    /**
     * {@code DELETE  /attendence-students-records/:id} : delete the "id" attendenceStudentsRecord.
     *
     * @param id the id of the attendenceStudentsRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAttendenceStudentsRecord(@PathVariable("id") String id) {
        LOG.debug("REST request to delete AttendenceStudentsRecord : {}", id);
        return attendenceStudentsRecordService
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
     * {@code SEARCH  /attendence-students-records/_search?query=:query} : search for the attendenceStudentsRecord corresponding
     * to the query.
     *
     * @param query the query of the attendenceStudentsRecord search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<AttendenceStudentsRecordDTO>>> searchAttendenceStudentsRecords(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of AttendenceStudentsRecords for query {}", query);
        return attendenceStudentsRecordService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(attendenceStudentsRecordService.search(query, pageable)));
    }
}
