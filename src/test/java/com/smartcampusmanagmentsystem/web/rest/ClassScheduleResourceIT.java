package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.ClassScheduleAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.ClassSchedule;
import com.smartcampusmanagmentsystem.repository.ClassScheduleRepository;
import com.smartcampusmanagmentsystem.repository.search.ClassScheduleSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ClassScheduleDTO;
import com.smartcampusmanagmentsystem.service.mapper.ClassScheduleMapper;
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
 * Integration tests for the {@link ClassScheduleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ClassScheduleResourceIT {

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MODULE_ID = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEDULE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEDULE_TIME_FROM = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE_TIME_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEDULE_TIME_TO = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE_TIME_TO = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/class-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/class-schedules/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private ClassScheduleMapper classScheduleMapper;

    @Autowired
    private ClassScheduleSearchRepository classScheduleSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ClassSchedule classSchedule;

    private ClassSchedule insertedClassSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassSchedule createEntity() {
        return new ClassSchedule()
            .courseId(DEFAULT_COURSE_ID)
            .moduleId(DEFAULT_MODULE_ID)
            .instructorId(DEFAULT_INSTRUCTOR_ID)
            .scheduleDate(DEFAULT_SCHEDULE_DATE)
            .scheduleTimeFrom(DEFAULT_SCHEDULE_TIME_FROM)
            .scheduleTimeTo(DEFAULT_SCHEDULE_TIME_TO)
            .location(DEFAULT_LOCATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassSchedule createUpdatedEntity() {
        return new ClassSchedule()
            .courseId(UPDATED_COURSE_ID)
            .moduleId(UPDATED_MODULE_ID)
            .instructorId(UPDATED_INSTRUCTOR_ID)
            .scheduleDate(UPDATED_SCHEDULE_DATE)
            .scheduleTimeFrom(UPDATED_SCHEDULE_TIME_FROM)
            .scheduleTimeTo(UPDATED_SCHEDULE_TIME_TO)
            .location(UPDATED_LOCATION);
    }

    @BeforeEach
    public void initTest() {
        classSchedule = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedClassSchedule != null) {
            classScheduleRepository.delete(insertedClassSchedule).block();
            classScheduleSearchRepository.delete(insertedClassSchedule).block();
            insertedClassSchedule = null;
        }
    }

    @Test
    void createClassSchedule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        // Create the ClassSchedule
        ClassScheduleDTO classScheduleDTO = classScheduleMapper.toDto(classSchedule);
        var returnedClassScheduleDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(classScheduleDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ClassScheduleDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ClassSchedule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClassSchedule = classScheduleMapper.toEntity(returnedClassScheduleDTO);
        assertClassScheduleUpdatableFieldsEquals(returnedClassSchedule, getPersistedClassSchedule(returnedClassSchedule));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedClassSchedule = returnedClassSchedule;
    }

    @Test
    void createClassScheduleWithExistingId() throws Exception {
        // Create the ClassSchedule with an existing ID
        classSchedule.setId("existing_id");
        ClassScheduleDTO classScheduleDTO = classScheduleMapper.toDto(classSchedule);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(classScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllClassSchedules() {
        // Initialize the database
        insertedClassSchedule = classScheduleRepository.save(classSchedule).block();

        // Get all the classScheduleList
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
            .value(hasItem(classSchedule.getId()))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].moduleId")
            .value(hasItem(DEFAULT_MODULE_ID))
            .jsonPath("$.[*].instructorId")
            .value(hasItem(DEFAULT_INSTRUCTOR_ID))
            .jsonPath("$.[*].scheduleDate")
            .value(hasItem(DEFAULT_SCHEDULE_DATE))
            .jsonPath("$.[*].scheduleTimeFrom")
            .value(hasItem(DEFAULT_SCHEDULE_TIME_FROM))
            .jsonPath("$.[*].scheduleTimeTo")
            .value(hasItem(DEFAULT_SCHEDULE_TIME_TO))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION));
    }

    @Test
    void getClassSchedule() {
        // Initialize the database
        insertedClassSchedule = classScheduleRepository.save(classSchedule).block();

        // Get the classSchedule
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, classSchedule.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(classSchedule.getId()))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.moduleId")
            .value(is(DEFAULT_MODULE_ID))
            .jsonPath("$.instructorId")
            .value(is(DEFAULT_INSTRUCTOR_ID))
            .jsonPath("$.scheduleDate")
            .value(is(DEFAULT_SCHEDULE_DATE))
            .jsonPath("$.scheduleTimeFrom")
            .value(is(DEFAULT_SCHEDULE_TIME_FROM))
            .jsonPath("$.scheduleTimeTo")
            .value(is(DEFAULT_SCHEDULE_TIME_TO))
            .jsonPath("$.location")
            .value(is(DEFAULT_LOCATION));
    }

    @Test
    void getNonExistingClassSchedule() {
        // Get the classSchedule
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingClassSchedule() throws Exception {
        // Initialize the database
        insertedClassSchedule = classScheduleRepository.save(classSchedule).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        classScheduleSearchRepository.save(classSchedule).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());

        // Update the classSchedule
        ClassSchedule updatedClassSchedule = classScheduleRepository.findById(classSchedule.getId()).block();
        updatedClassSchedule
            .courseId(UPDATED_COURSE_ID)
            .moduleId(UPDATED_MODULE_ID)
            .instructorId(UPDATED_INSTRUCTOR_ID)
            .scheduleDate(UPDATED_SCHEDULE_DATE)
            .scheduleTimeFrom(UPDATED_SCHEDULE_TIME_FROM)
            .scheduleTimeTo(UPDATED_SCHEDULE_TIME_TO)
            .location(UPDATED_LOCATION);
        ClassScheduleDTO classScheduleDTO = classScheduleMapper.toDto(updatedClassSchedule);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, classScheduleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(classScheduleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ClassSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClassScheduleToMatchAllProperties(updatedClassSchedule);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ClassSchedule> classScheduleSearchList = Streamable.of(
                    classScheduleSearchRepository.findAll().collectList().block()
                ).toList();
                ClassSchedule testClassScheduleSearch = classScheduleSearchList.get(searchDatabaseSizeAfter - 1);

                assertClassScheduleAllPropertiesEquals(testClassScheduleSearch, updatedClassSchedule);
            });
    }

    @Test
    void putNonExistingClassSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        classSchedule.setId(UUID.randomUUID().toString());

        // Create the ClassSchedule
        ClassScheduleDTO classScheduleDTO = classScheduleMapper.toDto(classSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, classScheduleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(classScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchClassSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        classSchedule.setId(UUID.randomUUID().toString());

        // Create the ClassSchedule
        ClassScheduleDTO classScheduleDTO = classScheduleMapper.toDto(classSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(classScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamClassSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        classSchedule.setId(UUID.randomUUID().toString());

        // Create the ClassSchedule
        ClassScheduleDTO classScheduleDTO = classScheduleMapper.toDto(classSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(classScheduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ClassSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateClassScheduleWithPatch() throws Exception {
        // Initialize the database
        insertedClassSchedule = classScheduleRepository.save(classSchedule).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classSchedule using partial update
        ClassSchedule partialUpdatedClassSchedule = new ClassSchedule();
        partialUpdatedClassSchedule.setId(classSchedule.getId());

        partialUpdatedClassSchedule.courseId(UPDATED_COURSE_ID).moduleId(UPDATED_MODULE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClassSchedule.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedClassSchedule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ClassSchedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassScheduleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClassSchedule, classSchedule),
            getPersistedClassSchedule(classSchedule)
        );
    }

    @Test
    void fullUpdateClassScheduleWithPatch() throws Exception {
        // Initialize the database
        insertedClassSchedule = classScheduleRepository.save(classSchedule).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classSchedule using partial update
        ClassSchedule partialUpdatedClassSchedule = new ClassSchedule();
        partialUpdatedClassSchedule.setId(classSchedule.getId());

        partialUpdatedClassSchedule
            .courseId(UPDATED_COURSE_ID)
            .moduleId(UPDATED_MODULE_ID)
            .instructorId(UPDATED_INSTRUCTOR_ID)
            .scheduleDate(UPDATED_SCHEDULE_DATE)
            .scheduleTimeFrom(UPDATED_SCHEDULE_TIME_FROM)
            .scheduleTimeTo(UPDATED_SCHEDULE_TIME_TO)
            .location(UPDATED_LOCATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClassSchedule.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedClassSchedule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ClassSchedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassScheduleUpdatableFieldsEquals(partialUpdatedClassSchedule, getPersistedClassSchedule(partialUpdatedClassSchedule));
    }

    @Test
    void patchNonExistingClassSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        classSchedule.setId(UUID.randomUUID().toString());

        // Create the ClassSchedule
        ClassScheduleDTO classScheduleDTO = classScheduleMapper.toDto(classSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, classScheduleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(classScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchClassSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        classSchedule.setId(UUID.randomUUID().toString());

        // Create the ClassSchedule
        ClassScheduleDTO classScheduleDTO = classScheduleMapper.toDto(classSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(classScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamClassSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        classSchedule.setId(UUID.randomUUID().toString());

        // Create the ClassSchedule
        ClassScheduleDTO classScheduleDTO = classScheduleMapper.toDto(classSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(classScheduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ClassSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteClassSchedule() {
        // Initialize the database
        insertedClassSchedule = classScheduleRepository.save(classSchedule).block();
        classScheduleRepository.save(classSchedule).block();
        classScheduleSearchRepository.save(classSchedule).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the classSchedule
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, classSchedule.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(classScheduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchClassSchedule() {
        // Initialize the database
        insertedClassSchedule = classScheduleRepository.save(classSchedule).block();
        classScheduleSearchRepository.save(classSchedule).block();

        // Search the classSchedule
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + classSchedule.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(classSchedule.getId()))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].moduleId")
            .value(hasItem(DEFAULT_MODULE_ID))
            .jsonPath("$.[*].instructorId")
            .value(hasItem(DEFAULT_INSTRUCTOR_ID))
            .jsonPath("$.[*].scheduleDate")
            .value(hasItem(DEFAULT_SCHEDULE_DATE))
            .jsonPath("$.[*].scheduleTimeFrom")
            .value(hasItem(DEFAULT_SCHEDULE_TIME_FROM))
            .jsonPath("$.[*].scheduleTimeTo")
            .value(hasItem(DEFAULT_SCHEDULE_TIME_TO))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION));
    }

    protected long getRepositoryCount() {
        return classScheduleRepository.count().block();
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

    protected ClassSchedule getPersistedClassSchedule(ClassSchedule classSchedule) {
        return classScheduleRepository.findById(classSchedule.getId()).block();
    }

    protected void assertPersistedClassScheduleToMatchAllProperties(ClassSchedule expectedClassSchedule) {
        assertClassScheduleAllPropertiesEquals(expectedClassSchedule, getPersistedClassSchedule(expectedClassSchedule));
    }

    protected void assertPersistedClassScheduleToMatchUpdatableProperties(ClassSchedule expectedClassSchedule) {
        assertClassScheduleAllUpdatablePropertiesEquals(expectedClassSchedule, getPersistedClassSchedule(expectedClassSchedule));
    }
}
