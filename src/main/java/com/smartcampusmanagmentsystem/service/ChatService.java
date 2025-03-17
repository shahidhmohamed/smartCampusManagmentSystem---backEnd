package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.ChatRepository;
import com.smartcampusmanagmentsystem.repository.search.ChatSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ChatDTO;
import com.smartcampusmanagmentsystem.service.mapper.ChatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.Chat}.
 */
@Service
public class ChatService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatService.class);

    private final ChatRepository chatRepository;

    private final ChatMapper chatMapper;

    private final ChatSearchRepository chatSearchRepository;

    public ChatService(ChatRepository chatRepository, ChatMapper chatMapper, ChatSearchRepository chatSearchRepository) {
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
        this.chatSearchRepository = chatSearchRepository;
    }

    /**
     * Save a chat.
     *
     * @param chatDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ChatDTO> save(ChatDTO chatDTO) {
        LOG.debug("Request to save Chat : {}", chatDTO);
        return chatRepository.save(chatMapper.toEntity(chatDTO)).flatMap(chatSearchRepository::save).map(chatMapper::toDto);
    }

    /**
     * Update a chat.
     *
     * @param chatDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ChatDTO> update(ChatDTO chatDTO) {
        LOG.debug("Request to update Chat : {}", chatDTO);
        return chatRepository.save(chatMapper.toEntity(chatDTO)).flatMap(chatSearchRepository::save).map(chatMapper::toDto);
    }

    /**
     * Partially update a chat.
     *
     * @param chatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ChatDTO> partialUpdate(ChatDTO chatDTO) {
        LOG.debug("Request to partially update Chat : {}", chatDTO);

        return chatRepository
            .findById(chatDTO.getId())
            .map(existingChat -> {
                chatMapper.partialUpdate(existingChat, chatDTO);

                return existingChat;
            })
            .flatMap(chatRepository::save)
            .flatMap(savedChat -> {
                chatSearchRepository.save(savedChat);
                return Mono.just(savedChat);
            })
            .map(chatMapper::toDto);
    }

    /**
     * Get all the chats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ChatDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Chats");
        return chatRepository.findAllBy(pageable).map(chatMapper::toDto);
    }

    /**
     * Returns the number of chats available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return chatRepository.count();
    }

    /**
     * Returns the number of chats available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return chatSearchRepository.count();
    }

    /**
     * Get one chat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ChatDTO> findOne(String id) {
        LOG.debug("Request to get Chat : {}", id);
        return chatRepository.findById(id).map(chatMapper::toDto);
    }

    /**
     * Delete the chat by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Chat : {}", id);
        return chatRepository.deleteById(id).then(chatSearchRepository.deleteById(id));
    }

    /**
     * Search for the chat corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ChatDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Chats for query {}", query);
        return chatSearchRepository.search(query, pageable).map(chatMapper::toDto);
    }
}
