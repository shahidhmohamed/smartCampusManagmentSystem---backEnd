package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.ResourceBookingAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.ResourceBooking;
import com.smartcampusmanagmentsystem.domain.enumeration.Status;
import com.smartcampusmanagmentsystem.repository.ResourceBookingRepository;
import com.smartcampusmanagmentsystem.repository.search.ResourceBookingSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ResourceBookingDTO;
import com.smartcampusmanagmentsystem.service.mapper.ResourceBookingMapper;
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
 * Integration tests for the {@link ResourceBookingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ResourceBookingResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_START_TIME = "AAAAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_ADMIN_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_ADMIN_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/resource-bookings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/resource-bookings/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResourceBookingRepository resourceBookingRepository;

    @Autowired
    private ResourceBookingMapper resourceBookingMapper;

    @Autowired
    private ResourceBookingSearchRepository resourceBookingSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ResourceBooking resourceBooking;

    private ResourceBooking insertedResourceBooking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceBooking createEntity() {
        return new ResourceBooking()
            .userId(DEFAULT_USER_ID)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .status(DEFAULT_STATUS)
            .reason(DEFAULT_REASON)
            .adminComment(DEFAULT_ADMIN_COMMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceBooking createUpdatedEntity() {
        return new ResourceBooking()
            .userId(UPDATED_USER_ID)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .reason(UPDATED_REASON)
            .adminComment(UPDATED_ADMIN_COMMENT);
    }

    @BeforeEach
    public void initTest() {
        resourceBooking = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedResourceBooking != null) {
            resourceBookingRepository.delete(insertedResourceBooking).block();
            resourceBookingSearchRepository.delete(insertedResourceBooking).block();
            insertedResourceBooking = null;
        }
    }

    @Test
    void createResourceBooking() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        // Create the ResourceBooking
        ResourceBookingDTO resourceBookingDTO = resourceBookingMapper.toDto(resourceBooking);
        var returnedResourceBookingDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceBookingDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ResourceBookingDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ResourceBooking in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResourceBooking = resourceBookingMapper.toEntity(returnedResourceBookingDTO);
        assertResourceBookingUpdatableFieldsEquals(returnedResourceBooking, getPersistedResourceBooking(returnedResourceBooking));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedResourceBooking = returnedResourceBooking;
    }

    @Test
    void createResourceBookingWithExistingId() throws Exception {
        // Create the ResourceBooking with an existing ID
        resourceBooking.setId("existing_id");
        ResourceBookingDTO resourceBookingDTO = resourceBookingMapper.toDto(resourceBooking);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceBookingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllResourceBookings() {
        // Initialize the database
        insertedResourceBooking = resourceBookingRepository.save(resourceBooking).block();

        // Get all the resourceBookingList
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
            .value(hasItem(resourceBooking.getId()))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].startTime")
            .value(hasItem(DEFAULT_START_TIME))
            .jsonPath("$.[*].endTime")
            .value(hasItem(DEFAULT_END_TIME))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].reason")
            .value(hasItem(DEFAULT_REASON))
            .jsonPath("$.[*].adminComment")
            .value(hasItem(DEFAULT_ADMIN_COMMENT));
    }

    @Test
    void getResourceBooking() {
        // Initialize the database
        insertedResourceBooking = resourceBookingRepository.save(resourceBooking).block();

        // Get the resourceBooking
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, resourceBooking.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(resourceBooking.getId()))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID))
            .jsonPath("$.startTime")
            .value(is(DEFAULT_START_TIME))
            .jsonPath("$.endTime")
            .value(is(DEFAULT_END_TIME))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.reason")
            .value(is(DEFAULT_REASON))
            .jsonPath("$.adminComment")
            .value(is(DEFAULT_ADMIN_COMMENT));
    }

    @Test
    void getNonExistingResourceBooking() {
        // Get the resourceBooking
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingResourceBooking() throws Exception {
        // Initialize the database
        insertedResourceBooking = resourceBookingRepository.save(resourceBooking).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceBookingSearchRepository.save(resourceBooking).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());

        // Update the resourceBooking
        ResourceBooking updatedResourceBooking = resourceBookingRepository.findById(resourceBooking.getId()).block();
        updatedResourceBooking
            .userId(UPDATED_USER_ID)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .reason(UPDATED_REASON)
            .adminComment(UPDATED_ADMIN_COMMENT);
        ResourceBookingDTO resourceBookingDTO = resourceBookingMapper.toDto(updatedResourceBooking);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resourceBookingDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceBookingDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ResourceBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResourceBookingToMatchAllProperties(updatedResourceBooking);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ResourceBooking> resourceBookingSearchList = Streamable.of(
                    resourceBookingSearchRepository.findAll().collectList().block()
                ).toList();
                ResourceBooking testResourceBookingSearch = resourceBookingSearchList.get(searchDatabaseSizeAfter - 1);

                assertResourceBookingAllPropertiesEquals(testResourceBookingSearch, updatedResourceBooking);
            });
    }

    @Test
    void putNonExistingResourceBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        resourceBooking.setId(UUID.randomUUID().toString());

        // Create the ResourceBooking
        ResourceBookingDTO resourceBookingDTO = resourceBookingMapper.toDto(resourceBooking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resourceBookingDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceBookingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchResourceBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        resourceBooking.setId(UUID.randomUUID().toString());

        // Create the ResourceBooking
        ResourceBookingDTO resourceBookingDTO = resourceBookingMapper.toDto(resourceBooking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceBookingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamResourceBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        resourceBooking.setId(UUID.randomUUID().toString());

        // Create the ResourceBooking
        ResourceBookingDTO resourceBookingDTO = resourceBookingMapper.toDto(resourceBooking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceBookingDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ResourceBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateResourceBookingWithPatch() throws Exception {
        // Initialize the database
        insertedResourceBooking = resourceBookingRepository.save(resourceBooking).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceBooking using partial update
        ResourceBooking partialUpdatedResourceBooking = new ResourceBooking();
        partialUpdatedResourceBooking.setId(resourceBooking.getId());

        partialUpdatedResourceBooking.userId(UPDATED_USER_ID).adminComment(UPDATED_ADMIN_COMMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResourceBooking.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedResourceBooking))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ResourceBooking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceBookingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResourceBooking, resourceBooking),
            getPersistedResourceBooking(resourceBooking)
        );
    }

    @Test
    void fullUpdateResourceBookingWithPatch() throws Exception {
        // Initialize the database
        insertedResourceBooking = resourceBookingRepository.save(resourceBooking).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceBooking using partial update
        ResourceBooking partialUpdatedResourceBooking = new ResourceBooking();
        partialUpdatedResourceBooking.setId(resourceBooking.getId());

        partialUpdatedResourceBooking
            .userId(UPDATED_USER_ID)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .reason(UPDATED_REASON)
            .adminComment(UPDATED_ADMIN_COMMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResourceBooking.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedResourceBooking))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ResourceBooking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceBookingUpdatableFieldsEquals(
            partialUpdatedResourceBooking,
            getPersistedResourceBooking(partialUpdatedResourceBooking)
        );
    }

    @Test
    void patchNonExistingResourceBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        resourceBooking.setId(UUID.randomUUID().toString());

        // Create the ResourceBooking
        ResourceBookingDTO resourceBookingDTO = resourceBookingMapper.toDto(resourceBooking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, resourceBookingDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(resourceBookingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchResourceBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        resourceBooking.setId(UUID.randomUUID().toString());

        // Create the ResourceBooking
        ResourceBookingDTO resourceBookingDTO = resourceBookingMapper.toDto(resourceBooking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(resourceBookingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamResourceBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        resourceBooking.setId(UUID.randomUUID().toString());

        // Create the ResourceBooking
        ResourceBookingDTO resourceBookingDTO = resourceBookingMapper.toDto(resourceBooking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(resourceBookingDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ResourceBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteResourceBooking() {
        // Initialize the database
        insertedResourceBooking = resourceBookingRepository.save(resourceBooking).block();
        resourceBookingRepository.save(resourceBooking).block();
        resourceBookingSearchRepository.save(resourceBooking).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the resourceBooking
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, resourceBooking.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceBookingSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchResourceBooking() {
        // Initialize the database
        insertedResourceBooking = resourceBookingRepository.save(resourceBooking).block();
        resourceBookingSearchRepository.save(resourceBooking).block();

        // Search the resourceBooking
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + resourceBooking.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(resourceBooking.getId()))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].startTime")
            .value(hasItem(DEFAULT_START_TIME))
            .jsonPath("$.[*].endTime")
            .value(hasItem(DEFAULT_END_TIME))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].reason")
            .value(hasItem(DEFAULT_REASON))
            .jsonPath("$.[*].adminComment")
            .value(hasItem(DEFAULT_ADMIN_COMMENT));
    }

    protected long getRepositoryCount() {
        return resourceBookingRepository.count().block();
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

    protected ResourceBooking getPersistedResourceBooking(ResourceBooking resourceBooking) {
        return resourceBookingRepository.findById(resourceBooking.getId()).block();
    }

    protected void assertPersistedResourceBookingToMatchAllProperties(ResourceBooking expectedResourceBooking) {
        assertResourceBookingAllPropertiesEquals(expectedResourceBooking, getPersistedResourceBooking(expectedResourceBooking));
    }

    protected void assertPersistedResourceBookingToMatchUpdatableProperties(ResourceBooking expectedResourceBooking) {
        assertResourceBookingAllUpdatablePropertiesEquals(expectedResourceBooking, getPersistedResourceBooking(expectedResourceBooking));
    }
}
