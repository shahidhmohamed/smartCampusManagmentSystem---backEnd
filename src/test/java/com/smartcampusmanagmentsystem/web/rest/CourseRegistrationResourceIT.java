package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.CourseRegistrationAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.CourseRegistration;
import com.smartcampusmanagmentsystem.repository.CourseRegistrationRepository;
import com.smartcampusmanagmentsystem.repository.search.CourseRegistrationSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.CourseRegistrationDTO;
import com.smartcampusmanagmentsystem.service.mapper.CourseRegistrationMapper;
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
 * Integration tests for the {@link CourseRegistrationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CourseRegistrationResourceIT {

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_DATE = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_DATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/course-registrations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/course-registrations/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CourseRegistrationRepository courseRegistrationRepository;

    @Autowired
    private CourseRegistrationMapper courseRegistrationMapper;

    @Autowired
    private CourseRegistrationSearchRepository courseRegistrationSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private CourseRegistration courseRegistration;

    private CourseRegistration insertedCourseRegistration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseRegistration createEntity() {
        return new CourseRegistration()
            .studentId(DEFAULT_STUDENT_ID)
            .courseId(DEFAULT_COURSE_ID)
            .courseCode(DEFAULT_COURSE_CODE)
            .duration(DEFAULT_DURATION)
            .registrationDate(DEFAULT_REGISTRATION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseRegistration createUpdatedEntity() {
        return new CourseRegistration()
            .studentId(UPDATED_STUDENT_ID)
            .courseId(UPDATED_COURSE_ID)
            .courseCode(UPDATED_COURSE_CODE)
            .duration(UPDATED_DURATION)
            .registrationDate(UPDATED_REGISTRATION_DATE);
    }

    @BeforeEach
    public void initTest() {
        courseRegistration = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCourseRegistration != null) {
            courseRegistrationRepository.delete(insertedCourseRegistration).block();
            courseRegistrationSearchRepository.delete(insertedCourseRegistration).block();
            insertedCourseRegistration = null;
        }
    }

    @Test
    void createCourseRegistration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        // Create the CourseRegistration
        CourseRegistrationDTO courseRegistrationDTO = courseRegistrationMapper.toDto(courseRegistration);
        var returnedCourseRegistrationDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(courseRegistrationDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CourseRegistrationDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the CourseRegistration in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCourseRegistration = courseRegistrationMapper.toEntity(returnedCourseRegistrationDTO);
        assertCourseRegistrationUpdatableFieldsEquals(
            returnedCourseRegistration,
            getPersistedCourseRegistration(returnedCourseRegistration)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCourseRegistration = returnedCourseRegistration;
    }

    @Test
    void createCourseRegistrationWithExistingId() throws Exception {
        // Create the CourseRegistration with an existing ID
        courseRegistration.setId("existing_id");
        CourseRegistrationDTO courseRegistrationDTO = courseRegistrationMapper.toDto(courseRegistration);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(courseRegistrationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CourseRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllCourseRegistrations() {
        // Initialize the database
        insertedCourseRegistration = courseRegistrationRepository.save(courseRegistration).block();

        // Get all the courseRegistrationList
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
            .value(hasItem(courseRegistration.getId()))
            .jsonPath("$.[*].studentId")
            .value(hasItem(DEFAULT_STUDENT_ID))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].courseCode")
            .value(hasItem(DEFAULT_COURSE_CODE))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION))
            .jsonPath("$.[*].registrationDate")
            .value(hasItem(DEFAULT_REGISTRATION_DATE));
    }

    @Test
    void getCourseRegistration() {
        // Initialize the database
        insertedCourseRegistration = courseRegistrationRepository.save(courseRegistration).block();

        // Get the courseRegistration
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, courseRegistration.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(courseRegistration.getId()))
            .jsonPath("$.studentId")
            .value(is(DEFAULT_STUDENT_ID))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.courseCode")
            .value(is(DEFAULT_COURSE_CODE))
            .jsonPath("$.duration")
            .value(is(DEFAULT_DURATION))
            .jsonPath("$.registrationDate")
            .value(is(DEFAULT_REGISTRATION_DATE));
    }

    @Test
    void getNonExistingCourseRegistration() {
        // Get the courseRegistration
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCourseRegistration() throws Exception {
        // Initialize the database
        insertedCourseRegistration = courseRegistrationRepository.save(courseRegistration).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseRegistrationSearchRepository.save(courseRegistration).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());

        // Update the courseRegistration
        CourseRegistration updatedCourseRegistration = courseRegistrationRepository.findById(courseRegistration.getId()).block();
        updatedCourseRegistration
            .studentId(UPDATED_STUDENT_ID)
            .courseId(UPDATED_COURSE_ID)
            .courseCode(UPDATED_COURSE_CODE)
            .duration(UPDATED_DURATION)
            .registrationDate(UPDATED_REGISTRATION_DATE);
        CourseRegistrationDTO courseRegistrationDTO = courseRegistrationMapper.toDto(updatedCourseRegistration);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, courseRegistrationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(courseRegistrationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CourseRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCourseRegistrationToMatchAllProperties(updatedCourseRegistration);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CourseRegistration> courseRegistrationSearchList = Streamable.of(
                    courseRegistrationSearchRepository.findAll().collectList().block()
                ).toList();
                CourseRegistration testCourseRegistrationSearch = courseRegistrationSearchList.get(searchDatabaseSizeAfter - 1);

                assertCourseRegistrationAllPropertiesEquals(testCourseRegistrationSearch, updatedCourseRegistration);
            });
    }

    @Test
    void putNonExistingCourseRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        courseRegistration.setId(UUID.randomUUID().toString());

        // Create the CourseRegistration
        CourseRegistrationDTO courseRegistrationDTO = courseRegistrationMapper.toDto(courseRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, courseRegistrationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(courseRegistrationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CourseRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchCourseRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        courseRegistration.setId(UUID.randomUUID().toString());

        // Create the CourseRegistration
        CourseRegistrationDTO courseRegistrationDTO = courseRegistrationMapper.toDto(courseRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(courseRegistrationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CourseRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamCourseRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        courseRegistration.setId(UUID.randomUUID().toString());

        // Create the CourseRegistration
        CourseRegistrationDTO courseRegistrationDTO = courseRegistrationMapper.toDto(courseRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(courseRegistrationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CourseRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateCourseRegistrationWithPatch() throws Exception {
        // Initialize the database
        insertedCourseRegistration = courseRegistrationRepository.save(courseRegistration).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courseRegistration using partial update
        CourseRegistration partialUpdatedCourseRegistration = new CourseRegistration();
        partialUpdatedCourseRegistration.setId(courseRegistration.getId());

        partialUpdatedCourseRegistration.registrationDate(UPDATED_REGISTRATION_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCourseRegistration.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCourseRegistration))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CourseRegistration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourseRegistrationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCourseRegistration, courseRegistration),
            getPersistedCourseRegistration(courseRegistration)
        );
    }

    @Test
    void fullUpdateCourseRegistrationWithPatch() throws Exception {
        // Initialize the database
        insertedCourseRegistration = courseRegistrationRepository.save(courseRegistration).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courseRegistration using partial update
        CourseRegistration partialUpdatedCourseRegistration = new CourseRegistration();
        partialUpdatedCourseRegistration.setId(courseRegistration.getId());

        partialUpdatedCourseRegistration
            .studentId(UPDATED_STUDENT_ID)
            .courseId(UPDATED_COURSE_ID)
            .courseCode(UPDATED_COURSE_CODE)
            .duration(UPDATED_DURATION)
            .registrationDate(UPDATED_REGISTRATION_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCourseRegistration.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCourseRegistration))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CourseRegistration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourseRegistrationUpdatableFieldsEquals(
            partialUpdatedCourseRegistration,
            getPersistedCourseRegistration(partialUpdatedCourseRegistration)
        );
    }

    @Test
    void patchNonExistingCourseRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        courseRegistration.setId(UUID.randomUUID().toString());

        // Create the CourseRegistration
        CourseRegistrationDTO courseRegistrationDTO = courseRegistrationMapper.toDto(courseRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, courseRegistrationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(courseRegistrationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CourseRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchCourseRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        courseRegistration.setId(UUID.randomUUID().toString());

        // Create the CourseRegistration
        CourseRegistrationDTO courseRegistrationDTO = courseRegistrationMapper.toDto(courseRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(courseRegistrationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CourseRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamCourseRegistration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        courseRegistration.setId(UUID.randomUUID().toString());

        // Create the CourseRegistration
        CourseRegistrationDTO courseRegistrationDTO = courseRegistrationMapper.toDto(courseRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(courseRegistrationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CourseRegistration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteCourseRegistration() {
        // Initialize the database
        insertedCourseRegistration = courseRegistrationRepository.save(courseRegistration).block();
        courseRegistrationRepository.save(courseRegistration).block();
        courseRegistrationSearchRepository.save(courseRegistration).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the courseRegistration
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, courseRegistration.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(courseRegistrationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchCourseRegistration() {
        // Initialize the database
        insertedCourseRegistration = courseRegistrationRepository.save(courseRegistration).block();
        courseRegistrationSearchRepository.save(courseRegistration).block();

        // Search the courseRegistration
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + courseRegistration.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(courseRegistration.getId()))
            .jsonPath("$.[*].studentId")
            .value(hasItem(DEFAULT_STUDENT_ID))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].courseCode")
            .value(hasItem(DEFAULT_COURSE_CODE))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION))
            .jsonPath("$.[*].registrationDate")
            .value(hasItem(DEFAULT_REGISTRATION_DATE));
    }

    protected long getRepositoryCount() {
        return courseRegistrationRepository.count().block();
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

    protected CourseRegistration getPersistedCourseRegistration(CourseRegistration courseRegistration) {
        return courseRegistrationRepository.findById(courseRegistration.getId()).block();
    }

    protected void assertPersistedCourseRegistrationToMatchAllProperties(CourseRegistration expectedCourseRegistration) {
        assertCourseRegistrationAllPropertiesEquals(expectedCourseRegistration, getPersistedCourseRegistration(expectedCourseRegistration));
    }

    protected void assertPersistedCourseRegistrationToMatchUpdatableProperties(CourseRegistration expectedCourseRegistration) {
        assertCourseRegistrationAllUpdatablePropertiesEquals(
            expectedCourseRegistration,
            getPersistedCourseRegistration(expectedCourseRegistration)
        );
    }
}
