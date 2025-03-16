package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.CampusEventAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.CampusEvent;
import com.smartcampusmanagmentsystem.domain.enumeration.EventStatus;
import com.smartcampusmanagmentsystem.repository.CampusEventRepository;
import com.smartcampusmanagmentsystem.repository.search.CampusEventSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.CampusEventDTO;
import com.smartcampusmanagmentsystem.service.mapper.CampusEventMapper;
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
 * Integration tests for the {@link CampusEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CampusEventResourceIT {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final EventStatus DEFAULT_STATUS = EventStatus.UPCOMING;
    private static final EventStatus UPDATED_STATUS = EventStatus.ONGOING;

    private static final String ENTITY_API_URL = "/api/campus-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/campus-events/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CampusEventRepository campusEventRepository;

    @Autowired
    private CampusEventMapper campusEventMapper;

    @Autowired
    private CampusEventSearchRepository campusEventSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private CampusEvent campusEvent;

    private CampusEvent insertedCampusEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CampusEvent createEntity() {
        return new CampusEvent()
            .eventName(DEFAULT_EVENT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .eventDate(DEFAULT_EVENT_DATE)
            .location(DEFAULT_LOCATION)
            .organizerId(DEFAULT_ORGANIZER_ID)
            .eventType(DEFAULT_EVENT_TYPE)
            .capacity(DEFAULT_CAPACITY)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CampusEvent createUpdatedEntity() {
        return new CampusEvent()
            .eventName(UPDATED_EVENT_NAME)
            .description(UPDATED_DESCRIPTION)
            .eventDate(UPDATED_EVENT_DATE)
            .location(UPDATED_LOCATION)
            .organizerId(UPDATED_ORGANIZER_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .capacity(UPDATED_CAPACITY)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        campusEvent = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCampusEvent != null) {
            campusEventRepository.delete(insertedCampusEvent).block();
            campusEventSearchRepository.delete(insertedCampusEvent).block();
            insertedCampusEvent = null;
        }
    }

    @Test
    void createCampusEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        // Create the CampusEvent
        CampusEventDTO campusEventDTO = campusEventMapper.toDto(campusEvent);
        var returnedCampusEventDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(campusEventDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CampusEventDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the CampusEvent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCampusEvent = campusEventMapper.toEntity(returnedCampusEventDTO);
        assertCampusEventUpdatableFieldsEquals(returnedCampusEvent, getPersistedCampusEvent(returnedCampusEvent));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCampusEvent = returnedCampusEvent;
    }

    @Test
    void createCampusEventWithExistingId() throws Exception {
        // Create the CampusEvent with an existing ID
        campusEvent.setId("existing_id");
        CampusEventDTO campusEventDTO = campusEventMapper.toDto(campusEvent);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(campusEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampusEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllCampusEvents() {
        // Initialize the database
        insertedCampusEvent = campusEventRepository.save(campusEvent).block();

        // Get all the campusEventList
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
            .value(hasItem(campusEvent.getId()))
            .jsonPath("$.[*].eventName")
            .value(hasItem(DEFAULT_EVENT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].eventDate")
            .value(hasItem(DEFAULT_EVENT_DATE))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION))
            .jsonPath("$.[*].organizerId")
            .value(hasItem(DEFAULT_ORGANIZER_ID))
            .jsonPath("$.[*].eventType")
            .value(hasItem(DEFAULT_EVENT_TYPE))
            .jsonPath("$.[*].capacity")
            .value(hasItem(DEFAULT_CAPACITY))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()));
    }

    @Test
    void getCampusEvent() {
        // Initialize the database
        insertedCampusEvent = campusEventRepository.save(campusEvent).block();

        // Get the campusEvent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, campusEvent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(campusEvent.getId()))
            .jsonPath("$.eventName")
            .value(is(DEFAULT_EVENT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.eventDate")
            .value(is(DEFAULT_EVENT_DATE))
            .jsonPath("$.location")
            .value(is(DEFAULT_LOCATION))
            .jsonPath("$.organizerId")
            .value(is(DEFAULT_ORGANIZER_ID))
            .jsonPath("$.eventType")
            .value(is(DEFAULT_EVENT_TYPE))
            .jsonPath("$.capacity")
            .value(is(DEFAULT_CAPACITY))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingCampusEvent() {
        // Get the campusEvent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCampusEvent() throws Exception {
        // Initialize the database
        insertedCampusEvent = campusEventRepository.save(campusEvent).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        campusEventSearchRepository.save(campusEvent).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());

        // Update the campusEvent
        CampusEvent updatedCampusEvent = campusEventRepository.findById(campusEvent.getId()).block();
        updatedCampusEvent
            .eventName(UPDATED_EVENT_NAME)
            .description(UPDATED_DESCRIPTION)
            .eventDate(UPDATED_EVENT_DATE)
            .location(UPDATED_LOCATION)
            .organizerId(UPDATED_ORGANIZER_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .capacity(UPDATED_CAPACITY)
            .status(UPDATED_STATUS);
        CampusEventDTO campusEventDTO = campusEventMapper.toDto(updatedCampusEvent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, campusEventDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(campusEventDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CampusEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCampusEventToMatchAllProperties(updatedCampusEvent);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CampusEvent> campusEventSearchList = Streamable.of(
                    campusEventSearchRepository.findAll().collectList().block()
                ).toList();
                CampusEvent testCampusEventSearch = campusEventSearchList.get(searchDatabaseSizeAfter - 1);

                assertCampusEventAllPropertiesEquals(testCampusEventSearch, updatedCampusEvent);
            });
    }

    @Test
    void putNonExistingCampusEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        campusEvent.setId(UUID.randomUUID().toString());

        // Create the CampusEvent
        CampusEventDTO campusEventDTO = campusEventMapper.toDto(campusEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, campusEventDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(campusEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampusEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchCampusEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        campusEvent.setId(UUID.randomUUID().toString());

        // Create the CampusEvent
        CampusEventDTO campusEventDTO = campusEventMapper.toDto(campusEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(campusEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampusEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamCampusEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        campusEvent.setId(UUID.randomUUID().toString());

        // Create the CampusEvent
        CampusEventDTO campusEventDTO = campusEventMapper.toDto(campusEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(campusEventDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CampusEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateCampusEventWithPatch() throws Exception {
        // Initialize the database
        insertedCampusEvent = campusEventRepository.save(campusEvent).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the campusEvent using partial update
        CampusEvent partialUpdatedCampusEvent = new CampusEvent();
        partialUpdatedCampusEvent.setId(campusEvent.getId());

        partialUpdatedCampusEvent
            .description(UPDATED_DESCRIPTION)
            .eventDate(UPDATED_EVENT_DATE)
            .organizerId(UPDATED_ORGANIZER_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .capacity(UPDATED_CAPACITY)
            .status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCampusEvent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCampusEvent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CampusEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCampusEventUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCampusEvent, campusEvent),
            getPersistedCampusEvent(campusEvent)
        );
    }

    @Test
    void fullUpdateCampusEventWithPatch() throws Exception {
        // Initialize the database
        insertedCampusEvent = campusEventRepository.save(campusEvent).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the campusEvent using partial update
        CampusEvent partialUpdatedCampusEvent = new CampusEvent();
        partialUpdatedCampusEvent.setId(campusEvent.getId());

        partialUpdatedCampusEvent
            .eventName(UPDATED_EVENT_NAME)
            .description(UPDATED_DESCRIPTION)
            .eventDate(UPDATED_EVENT_DATE)
            .location(UPDATED_LOCATION)
            .organizerId(UPDATED_ORGANIZER_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .capacity(UPDATED_CAPACITY)
            .status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCampusEvent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCampusEvent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CampusEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCampusEventUpdatableFieldsEquals(partialUpdatedCampusEvent, getPersistedCampusEvent(partialUpdatedCampusEvent));
    }

    @Test
    void patchNonExistingCampusEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        campusEvent.setId(UUID.randomUUID().toString());

        // Create the CampusEvent
        CampusEventDTO campusEventDTO = campusEventMapper.toDto(campusEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, campusEventDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(campusEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampusEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchCampusEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        campusEvent.setId(UUID.randomUUID().toString());

        // Create the CampusEvent
        CampusEventDTO campusEventDTO = campusEventMapper.toDto(campusEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(campusEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CampusEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamCampusEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        campusEvent.setId(UUID.randomUUID().toString());

        // Create the CampusEvent
        CampusEventDTO campusEventDTO = campusEventMapper.toDto(campusEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(campusEventDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CampusEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteCampusEvent() {
        // Initialize the database
        insertedCampusEvent = campusEventRepository.save(campusEvent).block();
        campusEventRepository.save(campusEvent).block();
        campusEventSearchRepository.save(campusEvent).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the campusEvent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, campusEvent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campusEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchCampusEvent() {
        // Initialize the database
        insertedCampusEvent = campusEventRepository.save(campusEvent).block();
        campusEventSearchRepository.save(campusEvent).block();

        // Search the campusEvent
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + campusEvent.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(campusEvent.getId()))
            .jsonPath("$.[*].eventName")
            .value(hasItem(DEFAULT_EVENT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].eventDate")
            .value(hasItem(DEFAULT_EVENT_DATE))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION))
            .jsonPath("$.[*].organizerId")
            .value(hasItem(DEFAULT_ORGANIZER_ID))
            .jsonPath("$.[*].eventType")
            .value(hasItem(DEFAULT_EVENT_TYPE))
            .jsonPath("$.[*].capacity")
            .value(hasItem(DEFAULT_CAPACITY))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()));
    }

    protected long getRepositoryCount() {
        return campusEventRepository.count().block();
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

    protected CampusEvent getPersistedCampusEvent(CampusEvent campusEvent) {
        return campusEventRepository.findById(campusEvent.getId()).block();
    }

    protected void assertPersistedCampusEventToMatchAllProperties(CampusEvent expectedCampusEvent) {
        assertCampusEventAllPropertiesEquals(expectedCampusEvent, getPersistedCampusEvent(expectedCampusEvent));
    }

    protected void assertPersistedCampusEventToMatchUpdatableProperties(CampusEvent expectedCampusEvent) {
        assertCampusEventAllUpdatablePropertiesEquals(expectedCampusEvent, getPersistedCampusEvent(expectedCampusEvent));
    }
}
