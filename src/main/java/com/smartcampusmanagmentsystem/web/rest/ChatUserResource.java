package com.smartcampusmanagmentsystem.web.rest;

import com.smartcampusmanagmentsystem.repository.ChatUserRepository;
import com.smartcampusmanagmentsystem.service.ChatUserService;
import com.smartcampusmanagmentsystem.service.dto.ChatUserDTO;
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
 * REST controller for managing {@link com.smartcampusmanagmentsystem.domain.ChatUser}.
 */
@RestController
@RequestMapping("/api/chat-users")
public class ChatUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChatUserResource.class);

    private static final String ENTITY_NAME = "chatUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatUserService chatUserService;

    private final ChatUserRepository chatUserRepository;

    public ChatUserResource(ChatUserService chatUserService, ChatUserRepository chatUserRepository) {
        this.chatUserService = chatUserService;
        this.chatUserRepository = chatUserRepository;
    }

    /**
     * {@code POST  /chat-users} : Create a new chatUser.
     *
     * @param chatUserDTO the chatUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatUserDTO, or with status {@code 400 (Bad Request)} if the chatUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ChatUserDTO>> createChatUser(@RequestBody ChatUserDTO chatUserDTO) throws URISyntaxException {
        LOG.debug("REST request to save ChatUser : {}", chatUserDTO);
        if (chatUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return chatUserService
            .save(chatUserDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/chat-users/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /chat-users/:id} : Updates an existing chatUser.
     *
     * @param id the id of the chatUserDTO to save.
     * @param chatUserDTO the chatUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatUserDTO,
     * or with status {@code 400 (Bad Request)} if the chatUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ChatUserDTO>> updateChatUser(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ChatUserDTO chatUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ChatUser : {}, {}", id, chatUserDTO);
        if (chatUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chatUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return chatUserService
                    .update(chatUserDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /chat-users/:id} : Partial updates given fields of an existing chatUser, field will ignore if it is null
     *
     * @param id the id of the chatUserDTO to save.
     * @param chatUserDTO the chatUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatUserDTO,
     * or with status {@code 400 (Bad Request)} if the chatUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chatUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chatUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ChatUserDTO>> partialUpdateChatUser(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ChatUserDTO chatUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ChatUser partially : {}, {}", id, chatUserDTO);
        if (chatUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return chatUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ChatUserDTO> result = chatUserService.partialUpdate(chatUserDTO);

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
     * {@code GET  /chat-users} : get all the chatUsers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatUsers in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ChatUserDTO>>> getAllChatUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of ChatUsers");
        return chatUserService
            .countAll()
            .zipWith(chatUserService.findAll(pageable).collectList())
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
     * {@code GET  /chat-users/:id} : get the "id" chatUser.
     *
     * @param id the id of the chatUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ChatUserDTO>> getChatUser(@PathVariable("id") String id) {
        LOG.debug("REST request to get ChatUser : {}", id);
        Mono<ChatUserDTO> chatUserDTO = chatUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatUserDTO);
    }

    /**
     * {@code DELETE  /chat-users/:id} : delete the "id" chatUser.
     *
     * @param id the id of the chatUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteChatUser(@PathVariable("id") String id) {
        LOG.debug("REST request to delete ChatUser : {}", id);
        return chatUserService
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
     * {@code SEARCH  /chat-users/_search?query=:query} : search for the chatUser corresponding
     * to the query.
     *
     * @param query the query of the chatUser search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<ChatUserDTO>>> searchChatUsers(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of ChatUsers for query {}", query);
        return chatUserService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(chatUserService.search(query, pageable)));
    }
}
