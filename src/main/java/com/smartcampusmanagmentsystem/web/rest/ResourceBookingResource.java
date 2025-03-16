package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.ResourceBookingRepository;
import com.smartcampusmanagmentsystem.service.ResourceBookingService;
import com.smartcampusmanagmentsystem.service.dto.ResourceBookingDTO;
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
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.ResourceBooking}.
 */
@RestController
@RequestMapping("/api/resource-bookings")
public class ResourceBookingResource {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceBookingResource.class);

    private static final String ENTITY_NAME = "resourceBooking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceBookingService resourceBookingService;

    private final ResourceBookingRepository resourceBookingRepository;

    public ResourceBookingResource(ResourceBookingService resourceBookingService, ResourceBookingRepository resourceBookingRepository) {
        this.resourceBookingService = resourceBookingService;
        this.resourceBookingRepository = resourceBookingRepository;
    }

    /**
     * {@code POST  /resource-bookings} : Create a new resourceBooking.
     *
     * @param resourceBookingDTO the resourceBookingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceBookingDTO, or with status {@code 400 (Bad Request)} if the resourceBooking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ResourceBookingDTO>> createResourceBooking(@RequestBody ResourceBookingDTO resourceBookingDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ResourceBooking : {}", resourceBookingDTO);
        if (resourceBookingDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceBooking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return resourceBookingService
            .save(resourceBookingDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/resource-bookings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /resource-bookings/:id} : Updates an existing resourceBooking.
     *
     * @param id the id of the resourceBookingDTO to save.
     * @param resourceBookingDTO the resourceBookingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceBookingDTO,
     * or with status {@code 400 (Bad Request)} if the resourceBookingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceBookingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResourceBookingDTO>> updateResourceBooking(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ResourceBookingDTO resourceBookingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ResourceBooking : {}, {}", id, resourceBookingDTO);
        if (resourceBookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceBookingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return resourceBookingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return resourceBookingService
                    .update(resourceBookingDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /resource-bookings/:id} : Partial updates given fields of an existing resourceBooking, field will ignore if it is null
     *
     * @param id the id of the resourceBookingDTO to save.
     * @param resourceBookingDTO the resourceBookingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceBookingDTO,
     * or with status {@code 400 (Bad Request)} if the resourceBookingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourceBookingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceBookingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ResourceBookingDTO>> partialUpdateResourceBooking(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ResourceBookingDTO resourceBookingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ResourceBooking partially : {}, {}", id, resourceBookingDTO);
        if (resourceBookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceBookingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return resourceBookingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ResourceBookingDTO> result = resourceBookingService.partialUpdate(resourceBookingDTO);

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
     * {@code GET  /resource-bookings} : get all the resourceBookings.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceBookings in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ResourceBookingDTO>>> getAllResourceBookings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of ResourceBookings");
        return resourceBookingService
            .countAll()
            .zipWith(resourceBookingService.findAll(pageable).collectList())
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
     * {@code GET  /resource-bookings/:id} : get the "id" resourceBooking.
     *
     * @param id the id of the resourceBookingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceBookingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResourceBookingDTO>> getResourceBooking(@PathVariable("id") String id) {
        LOG.debug("REST request to get ResourceBooking : {}", id);
        Mono<ResourceBookingDTO> resourceBookingDTO = resourceBookingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceBookingDTO);
    }

    /**
     * {@code DELETE  /resource-bookings/:id} : delete the "id" resourceBooking.
     *
     * @param id the id of the resourceBookingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteResourceBooking(@PathVariable("id") String id) {
        LOG.debug("REST request to delete ResourceBooking : {}", id);
        return resourceBookingService
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
     * {@code SEARCH  /resource-bookings/_search?query=:query} : search for the resourceBooking corresponding
     * to the query.
     *
     * @param query the query of the resourceBooking search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<ResourceBookingDTO>>> searchResourceBookings(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of ResourceBookings for query {}", query);
        return resourceBookingService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(resourceBookingService.search(query, pageable)));
    }
}
