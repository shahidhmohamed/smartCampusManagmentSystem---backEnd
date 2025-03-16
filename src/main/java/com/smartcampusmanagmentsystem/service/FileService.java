package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.FileRepository;
import com.smartcampusmanagmentsystem.repository.search.FileSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.FileDTO;
import com.smartcampusmanagmentsystem.service.mapper.FileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.File}.
 */
@Service
public class FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    private final FileSearchRepository fileSearchRepository;

    public FileService(FileRepository fileRepository, FileMapper fileMapper, FileSearchRepository fileSearchRepository) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.fileSearchRepository = fileSearchRepository;
    }

    /**
     * Save a file.
     *
     * @param fileDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FileDTO> save(FileDTO fileDTO) {
        LOG.debug("Request to save File : {}", fileDTO);
        return fileRepository.save(fileMapper.toEntity(fileDTO)).flatMap(fileSearchRepository::save).map(fileMapper::toDto);
    }

    /**
     * Update a file.
     *
     * @param fileDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FileDTO> update(FileDTO fileDTO) {
        LOG.debug("Request to update File : {}", fileDTO);
        return fileRepository.save(fileMapper.toEntity(fileDTO)).flatMap(fileSearchRepository::save).map(fileMapper::toDto);
    }

    /**
     * Partially update a file.
     *
     * @param fileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FileDTO> partialUpdate(FileDTO fileDTO) {
        LOG.debug("Request to partially update File : {}", fileDTO);

        return fileRepository
            .findById(fileDTO.getId())
            .map(existingFile -> {
                fileMapper.partialUpdate(existingFile, fileDTO);

                return existingFile;
            })
            .flatMap(fileRepository::save)
            .flatMap(savedFile -> {
                fileSearchRepository.save(savedFile);
                return Mono.just(savedFile);
            })
            .map(fileMapper::toDto);
    }

    /**
     * Get all the files.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<FileDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Files");
        return fileRepository.findAllBy(pageable).map(fileMapper::toDto);
    }

    /**
     * Returns the number of files available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return fileRepository.count();
    }

    /**
     * Returns the number of files available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return fileSearchRepository.count();
    }

    /**
     * Get one file by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<FileDTO> findOne(String id) {
        LOG.debug("Request to get File : {}", id);
        return fileRepository.findById(id).map(fileMapper::toDto);
    }

    /**
     * Delete the file by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete File : {}", id);
        return fileRepository.deleteById(id).then(fileSearchRepository.deleteById(id));
    }

    /**
     * Search for the file corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<FileDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Files for query {}", query);
        return fileSearchRepository.search(query, pageable).map(fileMapper::toDto);
    }
    
}
