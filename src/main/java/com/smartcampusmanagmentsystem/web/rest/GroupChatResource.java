package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.GroupChatRepository;
import com.smartcampusmanagmentsystem.service.GroupChatService;
import com.smartcampusmanagmentsystem.service.dto.GroupChatDTO;
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
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.GroupChat}.
 */
@RestController
@RequestMapping("/api/group-chats")
public class GroupChatResource {

    private static final Logger LOG = LoggerFactory.getLogger(GroupChatResource.class);

    private static final String ENTITY_NAME = "groupChat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupChatService groupChatService;

    private final GroupChatRepository groupChatRepository;

    public GroupChatResource(GroupChatService groupChatService, GroupChatRepository groupChatRepository) {
        this.groupChatService = groupChatService;
        this.groupChatRepository = groupChatRepository;
    }

    /**
     * {@code POST  /group-chats} : Create a new groupChat.
     *
     * @param groupChatDTO the groupChatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupChatDTO, or with status {@code 400 (Bad Request)} if the groupChat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<GroupChatDTO>> createGroupChat(@RequestBody GroupChatDTO groupChatDTO) throws URISyntaxException {
        LOG.debug("REST request to save GroupChat : {}", groupChatDTO);
        if (groupChatDTO.getId() != null) {
            throw new BadRequestAlertException("A new groupChat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return groupChatService
            .save(groupChatDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/group-chats/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /group-chats/:id} : Updates an existing groupChat.
     *
     * @param id the id of the groupChatDTO to save.
     * @param groupChatDTO the groupChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupChatDTO,
     * or with status {@code 400 (Bad Request)} if the groupChatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groupChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<GroupChatDTO>> updateGroupChat(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody GroupChatDTO groupChatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GroupChat : {}, {}", id, groupChatDTO);
        if (groupChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return groupChatRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return groupChatService
                    .update(groupChatDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /group-chats/:id} : Partial updates given fields of an existing groupChat, field will ignore if it is null
     *
     * @param id the id of the groupChatDTO to save.
     * @param groupChatDTO the groupChatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupChatDTO,
     * or with status {@code 400 (Bad Request)} if the groupChatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the groupChatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the groupChatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<GroupChatDTO>> partialUpdateGroupChat(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody GroupChatDTO groupChatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GroupChat partially : {}, {}", id, groupChatDTO);
        if (groupChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupChatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return groupChatRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<GroupChatDTO> result = groupChatService.partialUpdate(groupChatDTO);

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
     * {@code GET  /group-chats} : get all the groupChats.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupChats in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<GroupChatDTO>>> getAllGroupChats(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of GroupChats");
        return groupChatService
            .countAll()
            .zipWith(groupChatService.findAll(pageable).collectList())
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
     * {@code GET  /group-chats/:id} : get the "id" groupChat.
     *
     * @param id the id of the groupChatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupChatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<GroupChatDTO>> getGroupChat(@PathVariable("id") String id) {
        LOG.debug("REST request to get GroupChat : {}", id);
        Mono<GroupChatDTO> groupChatDTO = groupChatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groupChatDTO);
    }

    /**
     * {@code DELETE  /group-chats/:id} : delete the "id" groupChat.
     *
     * @param id the id of the groupChatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGroupChat(@PathVariable("id") String id) {
        LOG.debug("REST request to delete GroupChat : {}", id);
        return groupChatService
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
     * {@code SEARCH  /group-chats/_search?query=:query} : search for the groupChat corresponding
     * to the query.
     *
     * @param query the query of the groupChat search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<GroupChatDTO>>> searchGroupChats(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of GroupChats for query {}", query);
        return groupChatService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(groupChatService.search(query, pageable)));
    }
}
