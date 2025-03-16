package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.ResourceRepository;
import com.smartcampusmanagmentsystem.repository.search.ResourceSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ResourceDTO;
import com.smartcampusmanagmentsystem.service.mapper.ResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.Resource}.
 */
@Service
public class ResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceService.class);

    private final ResourceRepository resourceRepository;

    private final ResourceMapper resourceMapper;

    private final ResourceSearchRepository resourceSearchRepository;

    public ResourceService(
        ResourceRepository resourceRepository,
        ResourceMapper resourceMapper,
        ResourceSearchRepository resourceSearchRepository
    ) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
        this.resourceSearchRepository = resourceSearchRepository;
    }

    /**
     * Save a resource.
     *
     * @param resourceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResourceDTO> save(ResourceDTO resourceDTO) {
        LOG.debug("Request to save Resource : {}", resourceDTO);
        return resourceRepository
            .save(resourceMapper.toEntity(resourceDTO))
            .flatMap(resourceSearchRepository::save)
            .map(resourceMapper::toDto);
    }

    /**
     * Update a resource.
     *
     * @param resourceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResourceDTO> update(ResourceDTO resourceDTO) {
        LOG.debug("Request to update Resource : {}", resourceDTO);
        return resourceRepository
            .save(resourceMapper.toEntity(resourceDTO))
            .flatMap(resourceSearchRepository::save)
            .map(resourceMapper::toDto);
    }

    /**
     * Partially update a resource.
     *
     * @param resourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ResourceDTO> partialUpdate(ResourceDTO resourceDTO) {
        LOG.debug("Request to partially update Resource : {}", resourceDTO);

        return resourceRepository
            .findById(resourceDTO.getId())
            .map(existingResource -> {
                resourceMapper.partialUpdate(existingResource, resourceDTO);

                return existingResource;
            })
            .flatMap(resourceRepository::save)
            .flatMap(savedResource -> {
                resourceSearchRepository.save(savedResource);
                return Mono.just(savedResource);
            })
            .map(resourceMapper::toDto);
    }

    /**
     * Get all the resources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ResourceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Resources");
        return resourceRepository.findAllBy(pageable).map(resourceMapper::toDto);
    }

    /**
     * Returns the number of resources available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return resourceRepository.count();
    }

    /**
     * Returns the number of resources available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return resourceSearchRepository.count();
    }

    /**
     * Get one resource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ResourceDTO> findOne(String id) {
        LOG.debug("Request to get Resource : {}", id);
        return resourceRepository.findById(id).map(resourceMapper::toDto);
    }

    /**
     * Delete the resource by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Resource : {}", id);
        return resourceRepository.deleteById(id).then(resourceSearchRepository.deleteById(id));
    }

    /**
     * Search for the resource corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ResourceDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Resources for query {}", query);
        return resourceSearchRepository.search(query, pageable).map(resourceMapper::toDto);
    }
}
