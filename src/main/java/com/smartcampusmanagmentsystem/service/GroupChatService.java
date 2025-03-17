package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.GroupChatRepository;
import com.smartcampusmanagmentsystem.repository.search.GroupChatSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.GroupChatDTO;
import com.smartcampusmanagmentsystem.service.mapper.GroupChatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.GroupChat}.
 */
@Service
public class GroupChatService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupChatService.class);

    private final GroupChatRepository groupChatRepository;

    private final GroupChatMapper groupChatMapper;

    private final GroupChatSearchRepository groupChatSearchRepository;

    public GroupChatService(
        GroupChatRepository groupChatRepository,
        GroupChatMapper groupChatMapper,
        GroupChatSearchRepository groupChatSearchRepository
    ) {
        this.groupChatRepository = groupChatRepository;
        this.groupChatMapper = groupChatMapper;
        this.groupChatSearchRepository = groupChatSearchRepository;
    }

    /**
     * Save a groupChat.
     *
     * @param groupChatDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GroupChatDTO> save(GroupChatDTO groupChatDTO) {
        LOG.debug("Request to save GroupChat : {}", groupChatDTO);
        return groupChatRepository
            .save(groupChatMapper.toEntity(groupChatDTO))
            .flatMap(groupChatSearchRepository::save)
            .map(groupChatMapper::toDto);
    }

    /**
     * Update a groupChat.
     *
     * @param groupChatDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GroupChatDTO> update(GroupChatDTO groupChatDTO) {
        LOG.debug("Request to update GroupChat : {}", groupChatDTO);
        return groupChatRepository
            .save(groupChatMapper.toEntity(groupChatDTO))
            .flatMap(groupChatSearchRepository::save)
            .map(groupChatMapper::toDto);
    }

    /**
     * Partially update a groupChat.
     *
     * @param groupChatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<GroupChatDTO> partialUpdate(GroupChatDTO groupChatDTO) {
        LOG.debug("Request to partially update GroupChat : {}", groupChatDTO);

        return groupChatRepository
            .findById(groupChatDTO.getId())
            .map(existingGroupChat -> {
                groupChatMapper.partialUpdate(existingGroupChat, groupChatDTO);

                return existingGroupChat;
            })
            .flatMap(groupChatRepository::save)
            .flatMap(savedGroupChat -> {
                groupChatSearchRepository.save(savedGroupChat);
                return Mono.just(savedGroupChat);
            })
            .map(groupChatMapper::toDto);
    }

    /**
     * Get all the groupChats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<GroupChatDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all GroupChats");
        return groupChatRepository.findAllBy(pageable).map(groupChatMapper::toDto);
    }

    /**
     * Returns the number of groupChats available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return groupChatRepository.count();
    }

    /**
     * Returns the number of groupChats available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return groupChatSearchRepository.count();
    }

    /**
     * Get one groupChat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<GroupChatDTO> findOne(String id) {
        LOG.debug("Request to get GroupChat : {}", id);
        return groupChatRepository.findById(id).map(groupChatMapper::toDto);
    }

    /**
     * Delete the groupChat by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete GroupChat : {}", id);
        return groupChatRepository.deleteById(id).then(groupChatSearchRepository.deleteById(id));
    }

    /**
     * Search for the groupChat corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<GroupChatDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of GroupChats for query {}", query);
        return groupChatSearchRepository.search(query, pageable).map(groupChatMapper::toDto);
    }
}
