package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.AssignmentFileRepository;
import com.smartcampusmanagmentsystem.service.AssignmentFileService;
import com.smartcampusmanagmentsystem.service.dto.AssignmentFileDTO;
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
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.AssignmentFile}.
 */
@RestController
@RequestMapping("/api/assignment-files")
public class AssignmentFileResource {

    private static final Logger LOG = LoggerFactory.getLogger(AssignmentFileResource.class);

    private static final String ENTITY_NAME = "assignmentFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssignmentFileService assignmentFileService;

    private final AssignmentFileRepository assignmentFileRepository;

    public AssignmentFileResource(AssignmentFileService assignmentFileService, AssignmentFileRepository assignmentFileRepository) {
        this.assignmentFileService = assignmentFileService;
        this.assignmentFileRepository = assignmentFileRepository;
    }

    /**
     * {@code POST  /assignment-files} : Create a new assignmentFile.
     *
     * @param assignmentFileDTO the assignmentFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assignmentFileDTO, or with status {@code 400 (Bad Request)} if the assignmentFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AssignmentFileDTO>> createAssignmentFile(@RequestBody AssignmentFileDTO assignmentFileDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AssignmentFile : {}", assignmentFileDTO);
        if (assignmentFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new assignmentFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return assignmentFileService
            .save(assignmentFileDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/assignment-files/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /assignment-files/:id} : Updates an existing assignmentFile.
     *
     * @param id the id of the assignmentFileDTO to save.
     * @param assignmentFileDTO the assignmentFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignmentFileDTO,
     * or with status {@code 400 (Bad Request)} if the assignmentFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assignmentFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AssignmentFileDTO>> updateAssignmentFile(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AssignmentFileDTO assignmentFileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AssignmentFile : {}, {}", id, assignmentFileDTO);
        if (assignmentFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignmentFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assignmentFileRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return assignmentFileService
                    .update(assignmentFileDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /assignment-files/:id} : Partial updates given fields of an existing assignmentFile, field will ignore if it is null
     *
     * @param id the id of the assignmentFileDTO to save.
     * @param assignmentFileDTO the assignmentFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignmentFileDTO,
     * or with status {@code 400 (Bad Request)} if the assignmentFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assignmentFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assignmentFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AssignmentFileDTO>> partialUpdateAssignmentFile(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AssignmentFileDTO assignmentFileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AssignmentFile partially : {}, {}", id, assignmentFileDTO);
        if (assignmentFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignmentFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assignmentFileRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AssignmentFileDTO> result = assignmentFileService.partialUpdate(assignmentFileDTO);

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
     * {@code GET  /assignment-files} : get all the assignmentFiles.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assignmentFiles in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AssignmentFileDTO>>> getAllAssignmentFiles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of AssignmentFiles");
        return assignmentFileService
            .countAll()
            .zipWith(assignmentFileService.findAll(pageable).collectList())
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
     * {@code GET  /assignment-files/:id} : get the "id" assignmentFile.
     *
     * @param id the id of the assignmentFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assignmentFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AssignmentFileDTO>> getAssignmentFile(@PathVariable("id") String id) {
        LOG.debug("REST request to get AssignmentFile : {}", id);
        Mono<AssignmentFileDTO> assignmentFileDTO = assignmentFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assignmentFileDTO);
    }

    /**
     * {@code DELETE  /assignment-files/:id} : delete the "id" assignmentFile.
     *
     * @param id the id of the assignmentFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAssignmentFile(@PathVariable("id") String id) {
        LOG.debug("REST request to delete AssignmentFile : {}", id);
        return assignmentFileService
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
     * {@code SEARCH  /assignment-files/_search?query=:query} : search for the assignmentFile corresponding
     * to the query.
     *
     * @param query the query of the assignmentFile search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<AssignmentFileDTO>>> searchAssignmentFiles(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of AssignmentFiles for query {}", query);
        return assignmentFileService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(assignmentFileService.search(query, pageable)));
    }
}
