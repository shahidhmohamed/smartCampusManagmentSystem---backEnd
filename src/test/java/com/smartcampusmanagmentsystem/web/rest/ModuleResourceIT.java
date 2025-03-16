package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.ModuleAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.Module;
import com.smartcampusmanagmentsystem.repository.ModuleRepository;
import com.smartcampusmanagmentsystem.repository.search.ModuleSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ModuleDTO;
import com.smartcampusmanagmentsystem.service.mapper.ModuleMapper;
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
 * Integration tests for the {@link ModuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModuleResourceIT {

    private static final String DEFAULT_MODULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MODULE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SEMESTER = "AAAAAAAAAA";
    private static final String UPDATED_SEMESTER = "BBBBBBBBBB";

    private static final String DEFAULT_LECTURER_ID = "AAAAAAAAAA";
    private static final String UPDATED_LECTURER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/modules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/modules/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private ModuleSearchRepository moduleSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Module module;

    private Module insertedModule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createEntity() {
        return new Module()
            .moduleName(DEFAULT_MODULE_NAME)
            .moduleCode(DEFAULT_MODULE_CODE)
            .courseId(DEFAULT_COURSE_ID)
            .semester(DEFAULT_SEMESTER)
            .lecturerId(DEFAULT_LECTURER_ID)
            .duration(DEFAULT_DURATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createUpdatedEntity() {
        return new Module()
            .moduleName(UPDATED_MODULE_NAME)
            .moduleCode(UPDATED_MODULE_CODE)
            .courseId(UPDATED_COURSE_ID)
            .semester(UPDATED_SEMESTER)
            .lecturerId(UPDATED_LECTURER_ID)
            .duration(UPDATED_DURATION);
    }

    @BeforeEach
    public void initTest() {
        module = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedModule != null) {
            moduleRepository.delete(insertedModule).block();
            moduleSearchRepository.delete(insertedModule).block();
            insertedModule = null;
        }
    }

    @Test
    void createModule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);
        var returnedModuleDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ModuleDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Module in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedModule = moduleMapper.toEntity(returnedModuleDTO);
        assertModuleUpdatableFieldsEquals(returnedModule, getPersistedModule(returnedModule));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedModule = returnedModule;
    }

    @Test
    void createModuleWithExistingId() throws Exception {
        // Create the Module with an existing ID
        module.setId("existing_id");
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllModules() {
        // Initialize the database
        insertedModule = moduleRepository.save(module).block();

        // Get all the moduleList
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
            .value(hasItem(module.getId()))
            .jsonPath("$.[*].moduleName")
            .value(hasItem(DEFAULT_MODULE_NAME))
            .jsonPath("$.[*].moduleCode")
            .value(hasItem(DEFAULT_MODULE_CODE))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].semester")
            .value(hasItem(DEFAULT_SEMESTER))
            .jsonPath("$.[*].lecturerId")
            .value(hasItem(DEFAULT_LECTURER_ID))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION));
    }

    @Test
    void getModule() {
        // Initialize the database
        insertedModule = moduleRepository.save(module).block();

        // Get the module
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, module.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(module.getId()))
            .jsonPath("$.moduleName")
            .value(is(DEFAULT_MODULE_NAME))
            .jsonPath("$.moduleCode")
            .value(is(DEFAULT_MODULE_CODE))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.semester")
            .value(is(DEFAULT_SEMESTER))
            .jsonPath("$.lecturerId")
            .value(is(DEFAULT_LECTURER_ID))
            .jsonPath("$.duration")
            .value(is(DEFAULT_DURATION));
    }

    @Test
    void getNonExistingModule() {
        // Get the module
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModule() throws Exception {
        // Initialize the database
        insertedModule = moduleRepository.save(module).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        moduleSearchRepository.save(module).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());

        // Update the module
        Module updatedModule = moduleRepository.findById(module.getId()).block();
        updatedModule
            .moduleName(UPDATED_MODULE_NAME)
            .moduleCode(UPDATED_MODULE_CODE)
            .courseId(UPDATED_COURSE_ID)
            .semester(UPDATED_SEMESTER)
            .lecturerId(UPDATED_LECTURER_ID)
            .duration(UPDATED_DURATION);
        ModuleDTO moduleDTO = moduleMapper.toDto(updatedModule);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moduleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedModuleToMatchAllProperties(updatedModule);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Module> moduleSearchList = Streamable.of(moduleSearchRepository.findAll().collectList().block()).toList();
                Module testModuleSearch = moduleSearchList.get(searchDatabaseSizeAfter - 1);

                assertModuleAllPropertiesEquals(testModuleSearch, updatedModule);
            });
    }

    @Test
    void putNonExistingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        module.setId(UUID.randomUUID().toString());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moduleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        module.setId(UUID.randomUUID().toString());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        module.setId(UUID.randomUUID().toString());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        insertedModule = moduleRepository.save(module).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        partialUpdatedModule
            .moduleName(UPDATED_MODULE_NAME)
            .moduleCode(UPDATED_MODULE_CODE)
            .courseId(UPDATED_COURSE_ID)
            .lecturerId(UPDATED_LECTURER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModule.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedModule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Module in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModuleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedModule, module), getPersistedModule(module));
    }

    @Test
    void fullUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        insertedModule = moduleRepository.save(module).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        partialUpdatedModule
            .moduleName(UPDATED_MODULE_NAME)
            .moduleCode(UPDATED_MODULE_CODE)
            .courseId(UPDATED_COURSE_ID)
            .semester(UPDATED_SEMESTER)
            .lecturerId(UPDATED_LECTURER_ID)
            .duration(UPDATED_DURATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModule.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedModule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Module in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModuleUpdatableFieldsEquals(partialUpdatedModule, getPersistedModule(partialUpdatedModule));
    }

    @Test
    void patchNonExistingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        module.setId(UUID.randomUUID().toString());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, moduleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        module.setId(UUID.randomUUID().toString());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        module.setId(UUID.randomUUID().toString());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(moduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteModule() {
        // Initialize the database
        insertedModule = moduleRepository.save(module).block();
        moduleRepository.save(module).block();
        moduleSearchRepository.save(module).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the module
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, module.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moduleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchModule() {
        // Initialize the database
        insertedModule = moduleRepository.save(module).block();
        moduleSearchRepository.save(module).block();

        // Search the module
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + module.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(module.getId()))
            .jsonPath("$.[*].moduleName")
            .value(hasItem(DEFAULT_MODULE_NAME))
            .jsonPath("$.[*].moduleCode")
            .value(hasItem(DEFAULT_MODULE_CODE))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].semester")
            .value(hasItem(DEFAULT_SEMESTER))
            .jsonPath("$.[*].lecturerId")
            .value(hasItem(DEFAULT_LECTURER_ID))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION));
    }

    protected long getRepositoryCount() {
        return moduleRepository.count().block();
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

    protected Module getPersistedModule(Module module) {
        return moduleRepository.findById(module.getId()).block();
    }

    protected void assertPersistedModuleToMatchAllProperties(Module expectedModule) {
        assertModuleAllPropertiesEquals(expectedModule, getPersistedModule(expectedModule));
    }

    protected void assertPersistedModuleToMatchUpdatableProperties(Module expectedModule) {
        assertModuleAllUpdatablePropertiesEquals(expectedModule, getPersistedModule(expectedModule));
    }
}
