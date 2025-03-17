package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.GroupChatMembersRepository;
import com.smartcampusmanagmentsystem.service.GroupChatMembersService;
import com.smartcampusmanagmentsystem.service.dto.GroupChatMembersDTO;
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
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.GroupChatMembers}.
 */
@RestController
@RequestMapping("/api/group-chat-members")
public class GroupChatMembersResource {

    private static final Logger LOG = LoggerFactory.getLogger(GroupChatMembersResource.class);

    private static final String ENTITY_NAME = "groupChatMembers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupChatMembersService groupChatMembersService;

    private final GroupChatMembersRepository groupChatMembersRepository;

    public GroupChatMembersResource(
        GroupChatMembersService groupChatMembersService,
        GroupChatMembersRepository groupChatMembersRepository
    ) {
        this.groupChatMembersService = groupChatMembersService;
        this.groupChatMembersRepository = groupChatMembersRepository;
    }

    /**
     * {@code POST  /group-chat-members} : Create a new groupChatMembers.
     *
     * @param groupChatMembersDTO the groupChatMembersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupChatMembersDTO, or with status {@code 400 (Bad Request)} if the groupChatMembers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<GroupChatMembersDTO>> createGroupChatMembers(@RequestBody GroupChatMembersDTO groupChatMembersDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save GroupChatMembers : {}", groupChatMembersDTO);
        if (groupChatMembersDTO.getId() != null) {
            throw new BadRequestAlertException("A new groupChatMembers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return groupChatMembersService
            .save(groupChatMembersDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/group-chat-members/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /group-chat-members/:id} : Updates an existing groupChatMembers.
     *
     * @param id the id of the groupChatMembersDTO to save.
     * @param groupChatMembersDTO the groupChatMembersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupChatMembersDTO,
     * or with status {@code 400 (Bad Request)} if the groupChatMembersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groupChatMembersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<GroupChatMembersDTO>> updateGroupChatMembers(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody GroupChatMembersDTO groupChatMembersDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GroupChatMembers : {}, {}", id, groupChatMembersDTO);
        if (groupChatMembersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupChatMembersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return groupChatMembersRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return groupChatMembersService
                    .update(groupChatMembersDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /group-chat-members/:id} : Partial updates given fields of an existing groupChatMembers, field will ignore if it is null
     *
     * @param id the id of the groupChatMembersDTO to save.
     * @param groupChatMembersDTO the groupChatMembersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupChatMembersDTO,
     * or with status {@code 400 (Bad Request)} if the groupChatMembersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the groupChatMembersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the groupChatMembersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<GroupChatMembersDTO>> partialUpdateGroupChatMembers(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody GroupChatMembersDTO groupChatMembersDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GroupChatMembers partially : {}, {}", id, groupChatMembersDTO);
        if (groupChatMembersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupChatMembersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return groupChatMembersRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<GroupChatMembersDTO> result = groupChatMembersService.partialUpdate(groupChatMembersDTO);

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
     * {@code GET  /group-chat-members} : get all the groupChatMembers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupChatMembers in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<GroupChatMembersDTO>>> getAllGroupChatMembers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of GroupChatMembers");
        return groupChatMembersService
            .countAll()
            .zipWith(groupChatMembersService.findAll(pageable).collectList())
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
     * {@code GET  /group-chat-members/:id} : get the "id" groupChatMembers.
     *
     * @param id the id of the groupChatMembersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupChatMembersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<GroupChatMembersDTO>> getGroupChatMembers(@PathVariable("id") String id) {
        LOG.debug("REST request to get GroupChatMembers : {}", id);
        Mono<GroupChatMembersDTO> groupChatMembersDTO = groupChatMembersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groupChatMembersDTO);
    }

    /**
     * {@code DELETE  /group-chat-members/:id} : delete the "id" groupChatMembers.
     *
     * @param id the id of the groupChatMembersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGroupChatMembers(@PathVariable("id") String id) {
        LOG.debug("REST request to delete GroupChatMembers : {}", id);
        return groupChatMembersService
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
     * {@code SEARCH  /group-chat-members/_search?query=:query} : search for the groupChatMembers corresponding
     * to the query.
     *
     * @param query the query of the groupChatMembers search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<GroupChatMembersDTO>>> searchGroupChatMembers(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of GroupChatMembers for query {}", query);
        return groupChatMembersService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(groupChatMembersService.search(query, pageable)));
    }
}
