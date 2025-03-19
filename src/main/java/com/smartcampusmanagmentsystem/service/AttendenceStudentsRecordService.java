package com.smartcampusmanagmentsystem.service;

import com.smartcampusmanagmentsystem.repository.AttendenceStudentsRecordRepository;
import com.smartcampusmanagmentsystem.repository.search.AttendenceStudentsRecordSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.AttendenceStudentsRecordDTO;
import com.smartcampusmanagmentsystem.service.mapper.AttendenceStudentsRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecord}.
 */
@Service
public class AttendenceStudentsRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(AttendenceStudentsRecordService.class);

    private final AttendenceStudentsRecordRepository attendenceStudentsRecordRepository;

    private final AttendenceStudentsRecordMapper attendenceStudentsRecordMapper;

    private final AttendenceStudentsRecordSearchRepository attendenceStudentsRecordSearchRepository;

    public AttendenceStudentsRecordService(
        AttendenceStudentsRecordRepository attendenceStudentsRecordRepository,
        AttendenceStudentsRecordMapper attendenceStudentsRecordMapper,
        AttendenceStudentsRecordSearchRepository attendenceStudentsRecordSearchRepository
    ) {
        this.attendenceStudentsRecordRepository = attendenceStudentsRecordRepository;
        this.attendenceStudentsRecordMapper = attendenceStudentsRecordMapper;
        this.attendenceStudentsRecordSearchRepository = attendenceStudentsRecordSearchRepository;
    }

    /**
     * Save a attendenceStudentsRecord.
     *
     * @param attendenceStudentsRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AttendenceStudentsRecordDTO> save(AttendenceStudentsRecordDTO attendenceStudentsRecordDTO) {
        LOG.debug("Request to save AttendenceStudentsRecord : {}", attendenceStudentsRecordDTO);
        return attendenceStudentsRecordRepository
            .save(attendenceStudentsRecordMapper.toEntity(attendenceStudentsRecordDTO))
            .flatMap(attendenceStudentsRecordSearchRepository::save)
            .map(attendenceStudentsRecordMapper::toDto);
    }

    /**
     * Update a attendenceStudentsRecord.
     *
     * @param attendenceStudentsRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AttendenceStudentsRecordDTO> update(AttendenceStudentsRecordDTO attendenceStudentsRecordDTO) {
        LOG.debug("Request to update AttendenceStudentsRecord : {}", attendenceStudentsRecordDTO);
        return attendenceStudentsRecordRepository
            .save(attendenceStudentsRecordMapper.toEntity(attendenceStudentsRecordDTO))
            .flatMap(attendenceStudentsRecordSearchRepository::save)
            .map(attendenceStudentsRecordMapper::toDto);
    }

    /**
     * Partially update a attendenceStudentsRecord.
     *
     * @param attendenceStudentsRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AttendenceStudentsRecordDTO> partialUpdate(AttendenceStudentsRecordDTO attendenceStudentsRecordDTO) {
        LOG.debug("Request to partially update AttendenceStudentsRecord : {}", attendenceStudentsRecordDTO);

        return attendenceStudentsRecordRepository
            .findById(attendenceStudentsRecordDTO.getId())
            .map(existingAttendenceStudentsRecord -> {
                attendenceStudentsRecordMapper.partialUpdate(existingAttendenceStudentsRecord, attendenceStudentsRecordDTO);

                return existingAttendenceStudentsRecord;
            })
            .flatMap(attendenceStudentsRecordRepository::save)
            .flatMap(savedAttendenceStudentsRecord -> {
                attendenceStudentsRecordSearchRepository.save(savedAttendenceStudentsRecord);
                return Mono.just(savedAttendenceStudentsRecord);
            })
            .map(attendenceStudentsRecordMapper::toDto);
    }

    /**
     * Get all the attendenceStudentsRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AttendenceStudentsRecordDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AttendenceStudentsRecords");
        return attendenceStudentsRecordRepository.findAllBy(pageable).map(attendenceStudentsRecordMapper::toDto);
    }

    /**
     * Returns the number of attendenceStudentsRecords available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return attendenceStudentsRecordRepository.count();
    }

    /**
     * Returns the number of attendenceStudentsRecords available in search repository.
     *
     */
    public Mono<Long> searchCount() {
        return attendenceStudentsRecordSearchRepository.count();
    }

    /**
     * Get one attendenceStudentsRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<AttendenceStudentsRecordDTO> findOne(String id) {
        LOG.debug("Request to get AttendenceStudentsRecord : {}", id);
        return attendenceStudentsRecordRepository.findById(id).map(attendenceStudentsRecordMapper::toDto);
    }

    /**
     * Delete the attendenceStudentsRecord by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete AttendenceStudentsRecord : {}", id);
        return attendenceStudentsRecordRepository.deleteById(id).then(attendenceStudentsRecordSearchRepository.deleteById(id));
    }

    /**
     * Search for the attendenceStudentsRecord corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AttendenceStudentsRecordDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AttendenceStudentsRecords for query {}", query);
        return attendenceStudentsRecordSearchRepository.search(query, pageable).map(attendenceStudentsRecordMapper::toDto);
    }
}
