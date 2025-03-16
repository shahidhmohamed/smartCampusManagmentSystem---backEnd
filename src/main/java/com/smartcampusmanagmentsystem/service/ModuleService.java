package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.ModuleRepository;
import com.smartcampusmanagmentsystem.repository.search.ModuleSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ModuleDTO;
import com.smartcampusmanagmentsystem.service.mapper.ModuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.Module}.
 */
@Service
public class ModuleService {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleService.class);

    private final ModuleRepository moduleRepository;

    private final ModuleMapper moduleMapper;

    private final ModuleSearchRepository moduleSearchRepository;

    public ModuleService(ModuleRepository moduleRepository, ModuleMapper moduleMapper, ModuleSearchRepository moduleSearchRepository) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
        this.moduleSearchRepository = moduleSearchRepository;
    }

    /**
     * Save a module.
     *
     * @param moduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModuleDTO> save(ModuleDTO moduleDTO) {
        LOG.debug("Request to save Module : {}", moduleDTO);
        return moduleRepository.save(moduleMapper.toEntity(moduleDTO)).flatMap(moduleSearchRepository::save).map(moduleMapper::toDto);
    }

    /**
     * Update a module.
     *
     * @param moduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModuleDTO> update(ModuleDTO moduleDTO) {
        LOG.debug("Request to update Module : {}", moduleDTO);
        return moduleRepository.save(moduleMapper.toEntity(moduleDTO)).flatMap(moduleSearchRepository::save).map(moduleMapper::toDto);
    }

    /**
     * Partially update a module.
     *
     * @param moduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModuleDTO> partialUpdate(ModuleDTO moduleDTO) {
        LOG.debug("Request to partially update Module : {}", moduleDTO);

        return moduleRepository
            .findById(moduleDTO.getId())
            .map(existingModule -> {
                moduleMapper.partialUpdate(existingModule, moduleDTO);

                return existingModule;
            })
            .flatMap(moduleRepository::save)
            .flatMap(savedModule -> {
                moduleSearchRepository.save(savedModule);
                return Mono.just(savedModule);
            })
            .map(moduleMapper::toDto);
    }

    /**
     * Get all the modules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ModuleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Modules");
        return moduleRepository.findAllBy(pageable).map(moduleMapper::toDto);
    }

    /**
     * Returns the number of modules available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return moduleRepository.count();
    }

    /**
     * Returns the number of modules available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return moduleSearchRepository.count();
    }

    /**
     * Get one module by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ModuleDTO> findOne(String id) {
        LOG.debug("Request to get Module : {}", id);
        return moduleRepository.findById(id).map(moduleMapper::toDto);
    }

    /**
     * Delete the module by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Module : {}", id);
        return moduleRepository.deleteById(id).then(moduleSearchRepository.deleteById(id));
    }

    /**
     * Search for the module corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<ModuleDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Modules for query {}", query);
        return moduleSearchRepository.search(query, pageable).map(moduleMapper::toDto);
    }
}
