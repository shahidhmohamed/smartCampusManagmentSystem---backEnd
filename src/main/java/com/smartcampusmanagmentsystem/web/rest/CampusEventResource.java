package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.CampusEventRepository;
import com.smartcampusmanagmentsystem.service.CampusEventService;
import com.smartcampusmanagmentsystem.service.dto.CampusEventDTO;
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
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.CampusEvent}.
 */
@RestController
@RequestMapping("/api/campus-events")
public class CampusEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(CampusEventResource.class);

    private static final String ENTITY_NAME = "campusEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CampusEventService campusEventService;

    private final CampusEventRepository campusEventRepository;

    public CampusEventResource(CampusEventService campusEventService, CampusEventRepository campusEventRepository) {
        this.campusEventService = campusEventService;
        this.campusEventRepository = campusEventRepository;
    }

    /**
     * {@code POST  /campus-events} : Create a new campusEvent.
     *
     * @param campusEventDTO the campusEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new campusEventDTO, or with status {@code 400 (Bad Request)} if the campusEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CampusEventDTO>> createCampusEvent(@RequestBody CampusEventDTO campusEventDTO) throws URISyntaxException {
        LOG.debug("REST request to save CampusEvent : {}", campusEventDTO);
        if (campusEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new campusEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return campusEventService
            .save(campusEventDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/campus-events/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /campus-events/:id} : Updates an existing campusEvent.
     *
     * @param id the id of the campusEventDTO to save.
     * @param campusEventDTO the campusEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campusEventDTO,
     * or with status {@code 400 (Bad Request)} if the campusEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the campusEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CampusEventDTO>> updateCampusEvent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CampusEventDTO campusEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CampusEvent : {}, {}", id, campusEventDTO);
        if (campusEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campusEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return campusEventRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return campusEventService
                    .update(campusEventDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /campus-events/:id} : Partial updates given fields of an existing campusEvent, field will ignore if it is null
     *
     * @param id the id of the campusEventDTO to save.
     * @param campusEventDTO the campusEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campusEventDTO,
     * or with status {@code 400 (Bad Request)} if the campusEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the campusEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the campusEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CampusEventDTO>> partialUpdateCampusEvent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CampusEventDTO campusEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CampusEvent partially : {}, {}", id, campusEventDTO);
        if (campusEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campusEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return campusEventRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CampusEventDTO> result = campusEventService.partialUpdate(campusEventDTO);

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
     * {@code GET  /campus-events} : get all the campusEvents.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campusEvents in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CampusEventDTO>>> getAllCampusEvents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of CampusEvents");
        return campusEventService
            .countAll()
            .zipWith(campusEventService.findAll(pageable).collectList())
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
     * {@code GET  /campus-events/:id} : get the "id" campusEvent.
     *
     * @param id the id of the campusEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the campusEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CampusEventDTO>> getCampusEvent(@PathVariable("id") String id) {
        LOG.debug("REST request to get CampusEvent : {}", id);
        Mono<CampusEventDTO> campusEventDTO = campusEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(campusEventDTO);
    }

    /**
     * {@code DELETE  /campus-events/:id} : delete the "id" campusEvent.
     *
     * @param id the id of the campusEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCampusEvent(@PathVariable("id") String id) {
        LOG.debug("REST request to delete CampusEvent : {}", id);
        return campusEventService
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
     * {@code SEARCH  /campus-events/_search?query=:query} : search for the campusEvent corresponding
     * to the query.
     *
     * @param query the query of the campusEvent search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<CampusEventDTO>>> searchCampusEvents(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of CampusEvents for query {}", query);
        return campusEventService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(campusEventService.search(query, pageable)));
    }
}
