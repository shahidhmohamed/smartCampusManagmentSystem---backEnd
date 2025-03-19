package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.AttendenceAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.Attendence;
import com.smartcampusmanagmentsystem.repository.AttendenceRepository;
import com.smartcampusmanagmentsystem.repository.search.AttendenceSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.AttendenceDTO;
import com.smartcampusmanagmentsystem.service.mapper.AttendenceMapper;
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
 * Integration tests for the {@link AttendenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AttendenceResourceIT {

    private static final String DEFAULT_CREATED_AT = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MODULE_ID = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MODULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attendences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/attendences/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AttendenceRepository attendenceRepository;

    @Autowired
    private AttendenceMapper attendenceMapper;

    @Autowired
    private AttendenceSearchRepository attendenceSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Attendence attendence;

    private Attendence insertedAttendence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attendence createEntity() {
        return new Attendence()
            .createdAt(DEFAULT_CREATED_AT)
            .courseId(DEFAULT_COURSE_ID)
            .courseName(DEFAULT_COURSE_NAME)
            .instructorId(DEFAULT_INSTRUCTOR_ID)
            .instructorName(DEFAULT_INSTRUCTOR_NAME)
            .moduleId(DEFAULT_MODULE_ID)
            .moduleName(DEFAULT_MODULE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attendence createUpdatedEntity() {
        return new Attendence()
            .createdAt(UPDATED_CREATED_AT)
            .courseId(UPDATED_COURSE_ID)
            .courseName(UPDATED_COURSE_NAME)
            .instructorId(UPDATED_INSTRUCTOR_ID)
            .instructorName(UPDATED_INSTRUCTOR_NAME)
            .moduleId(UPDATED_MODULE_ID)
            .moduleName(UPDATED_MODULE_NAME);
    }

    @BeforeEach
    public void initTest() {
        attendence = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAttendence != null) {
            attendenceRepository.delete(insertedAttendence).block();
            attendenceSearchRepository.delete(insertedAttendence).block();
            insertedAttendence = null;
        }
    }

    @Test
    void createAttendence() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        // Create the Attendence
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);
        var returnedAttendenceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AttendenceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Attendence in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAttendence = attendenceMapper.toEntity(returnedAttendenceDTO);
        assertAttendenceUpdatableFieldsEquals(returnedAttendence, getPersistedAttendence(returnedAttendence));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAttendence = returnedAttendence;
    }

    @Test
    void createAttendenceWithExistingId() throws Exception {
        // Create the Attendence with an existing ID
        attendence.setId("existing_id");
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attendence in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAttendences() {
        // Initialize the database
        insertedAttendence = attendenceRepository.save(attendence).block();

        // Get all the attendenceList
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
            .value(hasItem(attendence.getId()))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].courseName")
            .value(hasItem(DEFAULT_COURSE_NAME))
            .jsonPath("$.[*].instructorId")
            .value(hasItem(DEFAULT_INSTRUCTOR_ID))
            .jsonPath("$.[*].instructorName")
            .value(hasItem(DEFAULT_INSTRUCTOR_NAME))
            .jsonPath("$.[*].moduleId")
            .value(hasItem(DEFAULT_MODULE_ID))
            .jsonPath("$.[*].moduleName")
            .value(hasItem(DEFAULT_MODULE_NAME));
    }

    @Test
    void getAttendence() {
        // Initialize the database
        insertedAttendence = attendenceRepository.save(attendence).block();

        // Get the attendence
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, attendence.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(attendence.getId()))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.courseName")
            .value(is(DEFAULT_COURSE_NAME))
            .jsonPath("$.instructorId")
            .value(is(DEFAULT_INSTRUCTOR_ID))
            .jsonPath("$.instructorName")
            .value(is(DEFAULT_INSTRUCTOR_NAME))
            .jsonPath("$.moduleId")
            .value(is(DEFAULT_MODULE_ID))
            .jsonPath("$.moduleName")
            .value(is(DEFAULT_MODULE_NAME));
    }

    @Test
    void getNonExistingAttendence() {
        // Get the attendence
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAttendence() throws Exception {
        // Initialize the database
        insertedAttendence = attendenceRepository.save(attendence).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        attendenceSearchRepository.save(attendence).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());

        // Update the attendence
        Attendence updatedAttendence = attendenceRepository.findById(attendence.getId()).block();
        updatedAttendence
            .createdAt(UPDATED_CREATED_AT)
            .courseId(UPDATED_COURSE_ID)
            .courseName(UPDATED_COURSE_NAME)
            .instructorId(UPDATED_INSTRUCTOR_ID)
            .instructorName(UPDATED_INSTRUCTOR_NAME)
            .moduleId(UPDATED_MODULE_ID)
            .moduleName(UPDATED_MODULE_NAME);
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(updatedAttendence);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attendenceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attendence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAttendenceToMatchAllProperties(updatedAttendence);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Attendence> attendenceSearchList = Streamable.of(attendenceSearchRepository.findAll().collectList().block()).toList();
                Attendence testAttendenceSearch = attendenceSearchList.get(searchDatabaseSizeAfter - 1);

                assertAttendenceAllPropertiesEquals(testAttendenceSearch, updatedAttendence);
            });
    }

    @Test
    void putNonExistingAttendence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        attendence.setId(UUID.randomUUID().toString());

        // Create the Attendence
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attendenceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attendence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAttendence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        attendence.setId(UUID.randomUUID().toString());

        // Create the Attendence
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attendence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAttendence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        attendence.setId(UUID.randomUUID().toString());

        // Create the Attendence
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(attendenceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Attendence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAttendenceWithPatch() throws Exception {
        // Initialize the database
        insertedAttendence = attendenceRepository.save(attendence).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attendence using partial update
        Attendence partialUpdatedAttendence = new Attendence();
        partialUpdatedAttendence.setId(attendence.getId());

        partialUpdatedAttendence.moduleId(UPDATED_MODULE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttendence.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAttendence))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attendence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttendenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAttendence, attendence),
            getPersistedAttendence(attendence)
        );
    }

    @Test
    void fullUpdateAttendenceWithPatch() throws Exception {
        // Initialize the database
        insertedAttendence = attendenceRepository.save(attendence).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attendence using partial update
        Attendence partialUpdatedAttendence = new Attendence();
        partialUpdatedAttendence.setId(attendence.getId());

        partialUpdatedAttendence
            .createdAt(UPDATED_CREATED_AT)
            .courseId(UPDATED_COURSE_ID)
            .courseName(UPDATED_COURSE_NAME)
            .instructorId(UPDATED_INSTRUCTOR_ID)
            .instructorName(UPDATED_INSTRUCTOR_NAME)
            .moduleId(UPDATED_MODULE_ID)
            .moduleName(UPDATED_MODULE_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttendence.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAttendence))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attendence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttendenceUpdatableFieldsEquals(partialUpdatedAttendence, getPersistedAttendence(partialUpdatedAttendence));
    }

    @Test
    void patchNonExistingAttendence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        attendence.setId(UUID.randomUUID().toString());

        // Create the Attendence
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, attendenceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(attendenceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attendence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAttendence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        attendence.setId(UUID.randomUUID().toString());

        // Create the Attendence
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(attendenceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attendence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAttendence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        attendence.setId(UUID.randomUUID().toString());

        // Create the Attendence
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(attendenceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Attendence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAttendence() {
        // Initialize the database
        insertedAttendence = attendenceRepository.save(attendence).block();
        attendenceRepository.save(attendence).block();
        attendenceSearchRepository.save(attendence).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the attendence
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, attendence.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(attendenceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAttendence() {
        // Initialize the database
        insertedAttendence = attendenceRepository.save(attendence).block();
        attendenceSearchRepository.save(attendence).block();

        // Search the attendence
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + attendence.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(attendence.getId()))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].courseName")
            .value(hasItem(DEFAULT_COURSE_NAME))
            .jsonPath("$.[*].instructorId")
            .value(hasItem(DEFAULT_INSTRUCTOR_ID))
            .jsonPath("$.[*].instructorName")
            .value(hasItem(DEFAULT_INSTRUCTOR_NAME))
            .jsonPath("$.[*].moduleId")
            .value(hasItem(DEFAULT_MODULE_ID))
            .jsonPath("$.[*].moduleName")
            .value(hasItem(DEFAULT_MODULE_NAME));
    }

    protected long getRepositoryCount() {
        return attendenceRepository.count().block();
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

    protected Attendence getPersistedAttendence(Attendence attendence) {
        return attendenceRepository.findById(attendence.getId()).block();
    }

    protected void assertPersistedAttendenceToMatchAllProperties(Attendence expectedAttendence) {
        assertAttendenceAllPropertiesEquals(expectedAttendence, getPersistedAttendence(expectedAttendence));
    }

    protected void assertPersistedAttendenceToMatchUpdatableProperties(Attendence expectedAttendence) {
        assertAttendenceAllUpdatablePropertiesEquals(expectedAttendence, getPersistedAttendence(expectedAttendence));
    }
}
