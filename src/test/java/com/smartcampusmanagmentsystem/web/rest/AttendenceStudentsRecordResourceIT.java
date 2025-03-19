package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecordAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecord;
import com.smartcampusmanagmentsystem.repository.AttendenceStudentsRecordRepository;
import com.smartcampusmanagmentsystem.repository.search.AttendenceStudentsRecordSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.AttendenceStudentsRecordDTO;
import com.smartcampusmanagmentsystem.service.mapper.AttendenceStudentsRecordMapper;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AttendenceStudentsRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AttendenceStudentsRecordResourceIT {

    private static final String DEFAULT_ATTENDENCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ATTENDENCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STUDENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRESENT = false;
    private static final Boolean UPDATED_IS_PRESENT = true;

    private static final String DEFAULT_CREATED_AT = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attendence-students-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/attendence-students-records/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AttendenceStudentsRecordRepository attendenceStudentsRecordRepository;

    @Autowired
    private AttendenceStudentsRecordMapper attendenceStudentsRecordMapper;

    @Autowired
    private AttendenceStudentsRecordSearchRepository attendenceStudentsRecordSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private AttendenceStudentsRecord attendenceStudentsRecord;

    private AttendenceStudentsRecord insertedAttendenceStudentsRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttendenceStudentsRecord createEntity() {
        return new AttendenceStudentsRecord()
            .attendenceId(DEFAULT_ATTENDENCE_ID)
            .studentId(DEFAULT_STUDENT_ID)
            .studentName(DEFAULT_STUDENT_NAME)
            .isPresent(DEFAULT_IS_PRESENT)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttendenceStudentsRecord createUpdatedEntity() {
        return new AttendenceStudentsRecord()
            .attendenceId(UPDATED_ATTENDENCE_ID)
            .studentId(UPDATED_STUDENT_ID)
            .studentName(UPDATED_STUDENT_NAME)
            .isPresent(UPDATED_IS_PRESENT)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    public void initTest() {
        attendenceStudentsRecord = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAttendenceStudentsRecord != null) {
            attendenceStudentsRecordRepository.delete(insertedAttendenceStudentsRecord).block();
            attendenceStudentsRecordSearchRepository.delete(insertedAttendenceStudentsRecord).block();
            insertedAttendenceStudentsRecord = null;
        }
    }

    @Test
    void createAttendenceStudentsRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        // Create the AttendenceStudentsRecord
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = attendenceStudentsRecordMapper.toDto(attendenceStudentsRecord);
        var returnedAttendenceStudentsRecordDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceStudentsRecordDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AttendenceStudentsRecordDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the AttendenceStudentsRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAttendenceStudentsRecord = attendenceStudentsRecordMapper.toEntity(returnedAttendenceStudentsRecordDTO);
        assertAttendenceStudentsRecordUpdatableFieldsEquals(
            returnedAttendenceStudentsRecord,
            getPersistedAttendenceStudentsRecord(returnedAttendenceStudentsRecord)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAttendenceStudentsRecord = returnedAttendenceStudentsRecord;
    }

    @Test
    void createAttendenceStudentsRecordWithExistingId() throws Exception {
        // Create the AttendenceStudentsRecord with an existing ID
        attendenceStudentsRecord.setId("existing_id");
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = attendenceStudentsRecordMapper.toDto(attendenceStudentsRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceStudentsRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttendenceStudentsRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAttendenceStudentsRecords() {
        // Initialize the database
        insertedAttendenceStudentsRecord = attendenceStudentsRecordRepository.save(attendenceStudentsRecord).block();

        // Get all the attendenceStudentsRecordList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(attendenceStudentsRecord.getId()))
            .jsonPath("$.[*].attendenceId")
            .value(hasItem(DEFAULT_ATTENDENCE_ID))
            .jsonPath("$.[*].studentId")
            .value(hasItem(DEFAULT_STUDENT_ID))
            .jsonPath("$.[*].studentName")
            .value(hasItem(DEFAULT_STUDENT_NAME))
            .jsonPath("$.[*].isPresent")
            .value(hasItem(DEFAULT_IS_PRESENT))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY));
    }

    @Test
    void getAttendenceStudentsRecord() {
        // Initialize the database
        insertedAttendenceStudentsRecord = attendenceStudentsRecordRepository.save(attendenceStudentsRecord).block();

        // Get the attendenceStudentsRecord
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, attendenceStudentsRecord.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(attendenceStudentsRecord.getId()))
            .jsonPath("$.attendenceId")
            .value(is(DEFAULT_ATTENDENCE_ID))
            .jsonPath("$.studentId")
            .value(is(DEFAULT_STUDENT_ID))
            .jsonPath("$.studentName")
            .value(is(DEFAULT_STUDENT_NAME))
            .jsonPath("$.isPresent")
            .value(is(DEFAULT_IS_PRESENT))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY));
    }

    @Test
    void getNonExistingAttendenceStudentsRecord() {
        // Get the attendenceStudentsRecord
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAttendenceStudentsRecord() throws Exception {
        // Initialize the database
        insertedAttendenceStudentsRecord = attendenceStudentsRecordRepository.save(attendenceStudentsRecord).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        attendenceStudentsRecordSearchRepository.save(attendenceStudentsRecord).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());

        // Update the attendenceStudentsRecord
        AttendenceStudentsRecord updatedAttendenceStudentsRecord = attendenceStudentsRecordRepository
            .findById(attendenceStudentsRecord.getId())
            .block();
        updatedAttendenceStudentsRecord
            .attendenceId(UPDATED_ATTENDENCE_ID)
            .studentId(UPDATED_STUDENT_ID)
            .studentName(UPDATED_STUDENT_NAME)
            .isPresent(UPDATED_IS_PRESENT)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = attendenceStudentsRecordMapper.toDto(updatedAttendenceStudentsRecord);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attendenceStudentsRecordDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceStudentsRecordDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AttendenceStudentsRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAttendenceStudentsRecordToMatchAllProperties(updatedAttendenceStudentsRecord);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AttendenceStudentsRecord> attendenceStudentsRecordSearchList = Streamable.of(
                    attendenceStudentsRecordSearchRepository.findAll().collectList().block()
                ).toList();
                AttendenceStudentsRecord testAttendenceStudentsRecordSearch = attendenceStudentsRecordSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertAttendenceStudentsRecordAllPropertiesEquals(testAttendenceStudentsRecordSearch, updatedAttendenceStudentsRecord);
            });
    }

    @Test
    void putNonExistingAttendenceStudentsRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        attendenceStudentsRecord.setId(UUID.randomUUID().toString());

        // Create the AttendenceStudentsRecord
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = attendenceStudentsRecordMapper.toDto(attendenceStudentsRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attendenceStudentsRecordDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceStudentsRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttendenceStudentsRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAttendenceStudentsRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        attendenceStudentsRecord.setId(UUID.randomUUID().toString());

        // Create the AttendenceStudentsRecord
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = attendenceStudentsRecordMapper.toDto(attendenceStudentsRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceStudentsRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttendenceStudentsRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAttendenceStudentsRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        attendenceStudentsRecord.setId(UUID.randomUUID().toString());

        // Create the AttendenceStudentsRecord
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = attendenceStudentsRecordMapper.toDto(attendenceStudentsRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceStudentsRecordDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AttendenceStudentsRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAttendenceStudentsRecordWithPatch() throws Exception {
        // Initialize the database
        insertedAttendenceStudentsRecord = attendenceStudentsRecordRepository.save(attendenceStudentsRecord).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attendenceStudentsRecord using partial update
        AttendenceStudentsRecord partialUpdatedAttendenceStudentsRecord = new AttendenceStudentsRecord();
        partialUpdatedAttendenceStudentsRecord.setId(attendenceStudentsRecord.getId());

        partialUpdatedAttendenceStudentsRecord
            .attendenceId(UPDATED_ATTENDENCE_ID)
            .studentId(UPDATED_STUDENT_ID)
            .studentName(UPDATED_STUDENT_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttendenceStudentsRecord.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAttendenceStudentsRecord))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AttendenceStudentsRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttendenceStudentsRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAttendenceStudentsRecord, attendenceStudentsRecord),
            getPersistedAttendenceStudentsRecord(attendenceStudentsRecord)
        );
    }

    @Test
    void fullUpdateAttendenceStudentsRecordWithPatch() throws Exception {
        // Initialize the database
        insertedAttendenceStudentsRecord = attendenceStudentsRecordRepository.save(attendenceStudentsRecord).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attendenceStudentsRecord using partial update
        AttendenceStudentsRecord partialUpdatedAttendenceStudentsRecord = new AttendenceStudentsRecord();
        partialUpdatedAttendenceStudentsRecord.setId(attendenceStudentsRecord.getId());

        partialUpdatedAttendenceStudentsRecord
            .attendenceId(UPDATED_ATTENDENCE_ID)
            .studentId(UPDATED_STUDENT_ID)
            .studentName(UPDATED_STUDENT_NAME)
            .isPresent(UPDATED_IS_PRESENT)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttendenceStudentsRecord.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAttendenceStudentsRecord))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AttendenceStudentsRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttendenceStudentsRecordUpdatableFieldsEquals(
            partialUpdatedAttendenceStudentsRecord,
            getPersistedAttendenceStudentsRecord(partialUpdatedAttendenceStudentsRecord)
        );
    }

    @Test
    void patchNonExistingAttendenceStudentsRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        attendenceStudentsRecord.setId(UUID.randomUUID().toString());

        // Create the AttendenceStudentsRecord
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = attendenceStudentsRecordMapper.toDto(attendenceStudentsRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, attendenceStudentsRecordDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(attendenceStudentsRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttendenceStudentsRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAttendenceStudentsRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        attendenceStudentsRecord.setId(UUID.randomUUID().toString());

        // Create the AttendenceStudentsRecord
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = attendenceStudentsRecordMapper.toDto(attendenceStudentsRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(attendenceStudentsRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttendenceStudentsRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAttendenceStudentsRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        attendenceStudentsRecord.setId(UUID.randomUUID().toString());

        // Create the AttendenceStudentsRecord
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = attendenceStudentsRecordMapper.toDto(attendenceStudentsRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(attendenceStudentsRecordDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AttendenceStudentsRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAttendenceStudentsRecord() {
        // Initialize the database
        insertedAttendenceStudentsRecord = attendenceStudentsRecordRepository.save(attendenceStudentsRecord).block();
        attendenceStudentsRecordRepository.save(attendenceStudentsRecord).block();
        attendenceStudentsRecordSearchRepository.save(attendenceStudentsRecord).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the attendenceStudentsRecord
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, attendenceStudentsRecord.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceStudentsRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAttendenceStudentsRecord() {
        // Initialize the database
        insertedAttendenceStudentsRecord = attendenceStudentsRecordRepository.save(attendenceStudentsRecord).block();
        attendenceStudentsRecordSearchRepository.save(attendenceStudentsRecord).block();

        // Search the attendenceStudentsRecord
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + attendenceStudentsRecord.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(attendenceStudentsRecord.getId()))
            .jsonPath("$.[*].attendenceId")
            .value(hasItem(DEFAULT_ATTENDENCE_ID))
            .jsonPath("$.[*].studentId")
            .value(hasItem(DEFAULT_STUDENT_ID))
            .jsonPath("$.[*].studentName")
            .value(hasItem(DEFAULT_STUDENT_NAME))
            .jsonPath("$.[*].isPresent")
            .value(hasItem(DEFAULT_IS_PRESENT))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY));
    }

    protected long getRepositoryCount() {
        return attendenceStudentsRecordRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AttendenceStudentsRecord getPersistedAttendenceStudentsRecord(AttendenceStudentsRecord attendenceStudentsRecord) {
        return attendenceStudentsRecordRepository.findById(attendenceStudentsRecord.getId()).block();
    }

    protected void assertPersistedAttendenceStudentsRecordToMatchAllProperties(AttendenceStudentsRecord expectedAttendenceStudentsRecord) {
        assertAttendenceStudentsRecordAllPropertiesEquals(
            expectedAttendenceStudentsRecord,
            getPersistedAttendenceStudentsRecord(expectedAttendenceStudentsRecord)
        );
    }

    protected void assertPersistedAttendenceStudentsRecordToMatchUpdatableProperties(
        AttendenceStudentsRecord expectedAttendenceStudentsRecord
    ) {
        assertAttendenceStudentsRecordAllUpdatablePropertiesEquals(
            expectedAttendenceStudentsRecord,
            getPersistedAttendenceStudentsRecord(expectedAttendenceStudentsRecord)
        );
    }
}
