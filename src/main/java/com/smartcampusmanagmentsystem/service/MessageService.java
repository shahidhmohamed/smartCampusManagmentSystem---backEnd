package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.MessageRepository;
import com.smartcampusmanagmentsystem.repository.search.MessageSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.MessageDTO;
import com.smartcampusmanagmentsystem.service.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.Message}.
 */
@Service
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    private final MessageSearchRepository messageSearchRepository;

    public MessageService(
        MessageRepository messageRepository,
        MessageMapper messageMapper,
        MessageSearchRepository messageSearchRepository
    ) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.messageSearchRepository = messageSearchRepository;
    }

    /**
     * Save a message.
     *
     * @param messageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MessageDTO> save(MessageDTO messageDTO) {
        LOG.debug("Request to save Message : {}", messageDTO);
        return messageRepository.save(messageMapper.toEntity(messageDTO)).flatMap(messageSearchRepository::save).map(messageMapper::toDto);
    }

    /**
     * Update a message.
     *
     * @param messageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MessageDTO> update(MessageDTO messageDTO) {
        LOG.debug("Request to update Message : {}", messageDTO);
        return messageRepository.save(messageMapper.toEntity(messageDTO)).flatMap(messageSearchRepository::save).map(messageMapper::toDto);
    }

    /**
     * Partially update a message.
     *
     * @param messageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MessageDTO> partialUpdate(MessageDTO messageDTO) {
        LOG.debug("Request to partially update Message : {}", messageDTO);

        return messageRepository
            .findById(messageDTO.getId())
            .map(existingMessage -> {
                messageMapper.partialUpdate(existingMessage, messageDTO);

                return existingMessage;
            })
            .flatMap(messageRepository::save)
            .flatMap(savedMessage -> {
                messageSearchRepository.save(savedMessage);
                return Mono.just(savedMessage);
            })
            .map(messageMapper::toDto);
    }

    /**
     * Get all the messages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<MessageDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Messages");
        return messageRepository.findAllBy(pageable).map(messageMapper::toDto);
    }

    /**
     * Returns the number of messages available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return messageRepository.count();
    }

    /**
     * Returns the number of messages available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return messageSearchRepository.count();
    }

    /**
     * Get one message by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<MessageDTO> findOne(String id) {
        LOG.debug("Request to get Message : {}", id);
        return messageRepository.findById(id).map(messageMapper::toDto);
    }

    /**
     * Delete the message by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Message : {}", id);
        return messageRepository.deleteById(id).then(messageSearchRepository.deleteById(id));
    }

    /**
     * Search for the message corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<MessageDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Messages for query {}", query);
        return messageSearchRepository.search(query, pageable).map(messageMapper::toDto);
    }
}
