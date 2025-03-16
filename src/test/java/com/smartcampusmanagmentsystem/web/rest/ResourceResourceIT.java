package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.ResourceAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.Resource;
import com.smartcampusmanagmentsystem.domain.enumeration.ResourceType;
import com.smartcampusmanagmentsystem.repository.ResourceRepository;
import com.smartcampusmanagmentsystem.repository.search.ResourceSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ResourceDTO;
import com.smartcampusmanagmentsystem.service.mapper.ResourceMapper;
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
 * Integration tests for the {@link ResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ResourceResourceIT {

    private static final String DEFAULT_RESOURCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ResourceType DEFAULT_RESOURCE_TYPE = ResourceType.ROOM;
    private static final ResourceType UPDATED_RESOURCE_TYPE = ResourceType.PROJECTOR;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final Boolean DEFAULT_AVAILABILITY = false;
    private static final Boolean UPDATED_AVAILABILITY = true;

    private static final String ENTITY_API_URL = "/api/resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/resources/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ResourceSearchRepository resourceSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Resource resource;

    private Resource insertedResource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createEntity() {
        return new Resource()
            .resourceId(DEFAULT_RESOURCE_ID)
            .name(DEFAULT_NAME)
            .resourceType(DEFAULT_RESOURCE_TYPE)
            .location(DEFAULT_LOCATION)
            .capacity(DEFAULT_CAPACITY)
            .availability(DEFAULT_AVAILABILITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createUpdatedEntity() {
        return new Resource()
            .resourceId(UPDATED_RESOURCE_ID)
            .name(UPDATED_NAME)
            .resourceType(UPDATED_RESOURCE_TYPE)
            .location(UPDATED_LOCATION)
            .capacity(UPDATED_CAPACITY)
            .availability(UPDATED_AVAILABILITY);
    }

    @BeforeEach
    public void initTest() {
        resource = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedResource != null) {
            resourceRepository.delete(insertedResource).block();
            resourceSearchRepository.delete(insertedResource).block();
            insertedResource = null;
        }
    }

    @Test
    void createResource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);
        var returnedResourceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ResourceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Resource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResource = resourceMapper.toEntity(returnedResourceDTO);
        assertResourceUpdatableFieldsEquals(returnedResource, getPersistedResource(returnedResource));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedResource = returnedResource;
    }

    @Test
    void createResourceWithExistingId() throws Exception {
        // Create the Resource with an existing ID
        resource.setId("existing_id");
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllResources() {
        // Initialize the database
        insertedResource = resourceRepository.save(resource).block();

        // Get all the resourceList
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
            .value(hasItem(resource.getId()))
            .jsonPath("$.[*].resourceId")
            .value(hasItem(DEFAULT_RESOURCE_ID))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].resourceType")
            .value(hasItem(DEFAULT_RESOURCE_TYPE.toString()))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION))
            .jsonPath("$.[*].capacity")
            .value(hasItem(DEFAULT_CAPACITY))
            .jsonPath("$.[*].availability")
            .value(hasItem(DEFAULT_AVAILABILITY));
    }

    @Test
    void getResource() {
        // Initialize the database
        insertedResource = resourceRepository.save(resource).block();

        // Get the resource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, resource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(resource.getId()))
            .jsonPath("$.resourceId")
            .value(is(DEFAULT_RESOURCE_ID))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.resourceType")
            .value(is(DEFAULT_RESOURCE_TYPE.toString()))
            .jsonPath("$.location")
            .value(is(DEFAULT_LOCATION))
            .jsonPath("$.capacity")
            .value(is(DEFAULT_CAPACITY))
            .jsonPath("$.availability")
            .value(is(DEFAULT_AVAILABILITY));
    }

    @Test
    void getNonExistingResource() {
        // Get the resource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingResource() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.save(resource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceSearchRepository.save(resource).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).block();
        updatedResource
            .resourceId(UPDATED_RESOURCE_ID)
            .name(UPDATED_NAME)
            .resourceType(UPDATED_RESOURCE_TYPE)
            .location(UPDATED_LOCATION)
            .capacity(UPDATED_CAPACITY)
            .availability(UPDATED_AVAILABILITY);
        ResourceDTO resourceDTO = resourceMapper.toDto(updatedResource);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResourceToMatchAllProperties(updatedResource);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Resource> resourceSearchList = Streamable.of(resourceSearchRepository.findAll().collectList().block()).toList();
                Resource testResourceSearch = resourceSearchList.get(searchDatabaseSizeAfter - 1);

                assertResourceAllPropertiesEquals(testResourceSearch, updatedResource);
            });
    }

    @Test
    void putNonExistingResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.save(resource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        partialUpdatedResource
            .name(UPDATED_NAME)
            .resourceType(UPDATED_RESOURCE_TYPE)
            .location(UPDATED_LOCATION)
            .availability(UPDATED_AVAILABILITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Resource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedResource, resource), getPersistedResource(resource));
    }

    @Test
    void fullUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.save(resource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        partialUpdatedResource
            .resourceId(UPDATED_RESOURCE_ID)
            .name(UPDATED_NAME)
            .resourceType(UPDATED_RESOURCE_TYPE)
            .location(UPDATED_LOCATION)
            .capacity(UPDATED_CAPACITY)
            .availability(UPDATED_AVAILABILITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Resource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceUpdatableFieldsEquals(partialUpdatedResource, getPersistedResource(partialUpdatedResource));
    }

    @Test
    void patchNonExistingResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, resourceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteResource() {
        // Initialize the database
        insertedResource = resourceRepository.save(resource).block();
        resourceRepository.save(resource).block();
        resourceSearchRepository.save(resource).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the resource
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, resource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(resourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchResource() {
        // Initialize the database
        insertedResource = resourceRepository.save(resource).block();
        resourceSearchRepository.save(resource).block();

        // Search the resource
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + resource.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(resource.getId()))
            .jsonPath("$.[*].resourceId")
            .value(hasItem(DEFAULT_RESOURCE_ID))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].resourceType")
            .value(hasItem(DEFAULT_RESOURCE_TYPE.toString()))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION))
            .jsonPath("$.[*].capacity")
            .value(hasItem(DEFAULT_CAPACITY))
            .jsonPath("$.[*].availability")
            .value(hasItem(DEFAULT_AVAILABILITY));
    }

    protected long getRepositoryCount() {
        return resourceRepository.count().block();
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

    protected Resource getPersistedResource(Resource resource) {
        return resourceRepository.findById(resource.getId()).block();
    }

    protected void assertPersistedResourceToMatchAllProperties(Resource expectedResource) {
        assertResourceAllPropertiesEquals(expectedResource, getPersistedResource(expectedResource));
    }

    protected void assertPersistedResourceToMatchUpdatableProperties(Resource expectedResource) {
        assertResourceAllUpdatablePropertiesEquals(expectedResource, getPersistedResource(expectedResource));
    }
}
