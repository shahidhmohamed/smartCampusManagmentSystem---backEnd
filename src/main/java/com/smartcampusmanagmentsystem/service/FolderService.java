package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.FolderRepository;
import com.smartcampusmanagmentsystem.repository.search.FolderSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.FolderDTO;
import com.smartcampusmanagmentsystem.service.mapper.FolderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.Folder}.
 */
@Service
public class FolderService {

    private static final Logger LOG = LoggerFactory.getLogger(FolderService.class);

    private final FolderRepository folderRepository;

    private final FolderMapper folderMapper;

    private final FolderSearchRepository folderSearchRepository;

    public FolderService(FolderRepository folderRepository, FolderMapper folderMapper, FolderSearchRepository folderSearchRepository) {
        this.folderRepository = folderRepository;
        this.folderMapper = folderMapper;
        this.folderSearchRepository = folderSearchRepository;
    }

    /**
     * Save a folder.
     *
     * @param folderDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FolderDTO> save(FolderDTO folderDTO) {
        LOG.debug("Request to save Folder : {}", folderDTO);
        return folderRepository.save(folderMapper.toEntity(folderDTO)).flatMap(folderSearchRepository::save).map(folderMapper::toDto);
    }

    /**
     * Update a folder.
     *
     * @param folderDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FolderDTO> update(FolderDTO folderDTO) {
        LOG.debug("Request to update Folder : {}", folderDTO);
        return folderRepository.save(folderMapper.toEntity(folderDTO)).flatMap(folderSearchRepository::save).map(folderMapper::toDto);
    }

    /**
     * Partially update a folder.
     *
     * @param folderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FolderDTO> partialUpdate(FolderDTO folderDTO) {
        LOG.debug("Request to partially update Folder : {}", folderDTO);

        return folderRepository
            .findById(folderDTO.getId())
            .map(existingFolder -> {
                folderMapper.partialUpdate(existingFolder, folderDTO);

                return existingFolder;
            })
            .flatMap(folderRepository::save)
            .flatMap(savedFolder -> {
                folderSearchRepository.save(savedFolder);
                return Mono.just(savedFolder);
            })
            .map(folderMapper::toDto);
    }

    /**
     * Get all the folders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<FolderDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Folders");
        return folderRepository.findAllBy(pageable).map(folderMapper::toDto);
    }

    /**
     * Returns the number of folders available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return folderRepository.count();
    }

    /**
     * Returns the number of folders available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return folderSearchRepository.count();
    }

    /**
     * Get one folder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<FolderDTO> findOne(String id) {
        LOG.debug("Request to get Folder : {}", id);
        return folderRepository.findById(id).map(folderMapper::toDto);
    }

    /**
     * Delete the folder by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Folder : {}", id);
        return folderRepository.deleteById(id).then(folderSearchRepository.deleteById(id));
    }

    /**
     * Search for the folder corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<FolderDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Folders for query {}", query);
        return folderSearchRepository.search(query, pageable).map(folderMapper::toDto);
    }
}
