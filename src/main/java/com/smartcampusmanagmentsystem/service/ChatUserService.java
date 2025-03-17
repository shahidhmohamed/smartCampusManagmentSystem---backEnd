package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.ChatUserRepository;
import com.smartcampusmanagmentsystem.repository.search.ChatUserSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ChatUserDTO;
import com.smartcampusmanagmentsystem.service.mapper.ChatUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.ChatUser}.
 */
@Service
public class ChatUserService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatUserService.class);

    private final ChatUserRepository chatUserRepository;

    private final ChatUserMapper chatUserMapper;

    private final ChatUserSearchRepository chatUserSearchRepository;

    public ChatUserService(
        ChatUserRepository chatUserRepository,
        ChatUserMapper chatUserMapper,
        ChatUserSearchRepository chatUserSearchRepository
    ) {
        this.chatUserRepository = chatUserRepository;
        this.chatUserMapper = chatUserMapper;
        this.chatUserSearchRepository = chatUserSearchRepository;
    }

    /**
     * Save a chatUser.
     *
     * @param chatUserDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ChatUserDTO> save(ChatUserDTO chatUserDTO) {
        LOG.debug("Request to save ChatUser : {}", chatUserDTO);
        return chatUserRepository
            .save(chatUserMapper.toEntity(chatUserDTO))
            .flatMap(chatUserSearchRepository::save)
            .map(chatUserMapper::toDto);
    }

    /**
     * Update a chatUser.
     *
     * @param chatUserDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ChatUserDTO> update(ChatUserDTO chatUserDTO) {
        LOG.debug("Request to update ChatUser : {}", chatUserDTO);
        return chatUserRepository
            .save(chatUserMapper.toEntity(chatUserDTO))
            .flatMap(chatUserSearchRepository::save)
            .map(chatUserMapper::toDto);
    }

    /**
     * Partially update a chatUser.
     *
     * @param chatUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ChatUserDTO> partialUpdate(ChatUserDTO chatUserDTO) {
        LOG.debug("Request to partially update ChatUser : {}", chatUserDTO);

        return chatUserRepository
            .findById(chatUserDTO.getId())
            .map(existingChatUser -> {
                chatUserMapper.partialUpdate(existingChatUser, chatUserDTO);

                return existingChatUser;
            })
            .flatMap(chatUserRepository::save)
            .flatMap(savedChatUser -> {
                chatUserSearchRepository.save(savedChatUser);
                return Mono.just(savedChatUser);
            })
            .map(chatUserMapper::toDto);
    }

    /**
     * Get all the chatUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ChatUserDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ChatUsers");
        return chatUserRepository.findAllBy(pageable).map(chatUserMapper::toDto);
    }

    /**
     * Returns the number of chatUsers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return chatUserRepository.count();
    }

    /**
     * Returns the number of chatUsers available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return chatUserSearchRepository.count();
    }

    /**
     * Get one chatUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ChatUserDTO> findOne(String id) {
        LOG.debug("Request to get ChatUser : {}", id);
        return chatUserRepository.findById(id).map(chatUserMapper::toDto);
    }

    /**
     * Delete the chatUser by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete ChatUser : {}", id);
        return chatUserRepository.deleteById(id).then(chatUserSearchRepository.deleteById(id));
    }

    /**
     * Search for the chatUser corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ChatUserDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ChatUsers for query {}", query);
        return chatUserSearchRepository.search(query, pageable).map(chatUserMapper::toDto);
    }
}
