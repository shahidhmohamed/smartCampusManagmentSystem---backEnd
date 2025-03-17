package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.GroupChatMembersRepository;
import com.smartcampusmanagmentsystem.repository.search.GroupChatMembersSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.GroupChatMembersDTO;
import com.smartcampusmanagmentsystem.service.mapper.GroupChatMembersMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.GroupChatMembers}.
 */
@Service
public class GroupChatMembersService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupChatMembersService.class);

    private final GroupChatMembersRepository groupChatMembersRepository;

    private final GroupChatMembersMapper groupChatMembersMapper;

    private final GroupChatMembersSearchRepository groupChatMembersSearchRepository;

    public GroupChatMembersService(
        GroupChatMembersRepository groupChatMembersRepository,
        GroupChatMembersMapper groupChatMembersMapper,
        GroupChatMembersSearchRepository groupChatMembersSearchRepository
    ) {
        this.groupChatMembersRepository = groupChatMembersRepository;
        this.groupChatMembersMapper = groupChatMembersMapper;
        this.groupChatMembersSearchRepository = groupChatMembersSearchRepository;
    }

    /**
     * Save a groupChatMembers.
     *
     * @param groupChatMembersDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GroupChatMembersDTO> save(GroupChatMembersDTO groupChatMembersDTO) {
        LOG.debug("Request to save GroupChatMembers : {}", groupChatMembersDTO);
        return groupChatMembersRepository
            .save(groupChatMembersMapper.toEntity(groupChatMembersDTO))
            .flatMap(groupChatMembersSearchRepository::save)
            .map(groupChatMembersMapper::toDto);
    }

    /**
     * Update a groupChatMembers.
     *
     * @param groupChatMembersDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GroupChatMembersDTO> update(GroupChatMembersDTO groupChatMembersDTO) {
        LOG.debug("Request to update GroupChatMembers : {}", groupChatMembersDTO);
        return groupChatMembersRepository
            .save(groupChatMembersMapper.toEntity(groupChatMembersDTO))
            .flatMap(groupChatMembersSearchRepository::save)
            .map(groupChatMembersMapper::toDto);
    }

    /**
     * Partially update a groupChatMembers.
     *
     * @param groupChatMembersDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<GroupChatMembersDTO> partialUpdate(GroupChatMembersDTO groupChatMembersDTO) {
        LOG.debug("Request to partially update GroupChatMembers : {}", groupChatMembersDTO);

        return groupChatMembersRepository
            .findById(groupChatMembersDTO.getId())
            .map(existingGroupChatMembers -> {
                groupChatMembersMapper.partialUpdate(existingGroupChatMembers, groupChatMembersDTO);

                return existingGroupChatMembers;
            })
            .flatMap(groupChatMembersRepository::save)
            .flatMap(savedGroupChatMembers -> {
                groupChatMembersSearchRepository.save(savedGroupChatMembers);
                return Mono.just(savedGroupChatMembers);
            })
            .map(groupChatMembersMapper::toDto);
    }

    /**
     * Get all the groupChatMembers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<GroupChatMembersDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all GroupChatMembers");
        return groupChatMembersRepository.findAllBy(pageable).map(groupChatMembersMapper::toDto);
    }

    /**
     * Returns the number of groupChatMembers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return groupChatMembersRepository.count();
    }

    /**
     * Returns the number of groupChatMembers available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return groupChatMembersSearchRepository.count();
    }

    /**
     * Get one groupChatMembers by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<GroupChatMembersDTO> findOne(String id) {
        LOG.debug("Request to get GroupChatMembers : {}", id);
        return groupChatMembersRepository.findById(id).map(groupChatMembersMapper::toDto);
    }

    /**
     * Delete the groupChatMembers by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete GroupChatMembers : {}", id);
        return groupChatMembersRepository.deleteById(id).then(groupChatMembersSearchRepository.deleteById(id));
    }

    /**
     * Search for the groupChatMembers corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<GroupChatMembersDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of GroupChatMembers for query {}", query);
        return groupChatMembersSearchRepository.search(query, pageable).map(groupChatMembersMapper::toDto);
    }
}
