package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.FileAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.File;
import com.smartcampusmanagmentsystem.repository.FileRepository;
import com.smartcampusmanagmentsystem.repository.search.FileSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.FileDTO;
import com.smartcampusmanagmentsystem.service.mapper.FileMapper;
import java.util.Base64;
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
 * Integration tests for the {@link FileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FileResourceIT {

    private static final String DEFAULT_FOLDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_FOLDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FILE_SIZE = 1;
    private static final Integer UPDATED_FILE_SIZE = 2;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_AT = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_MODIFIED_AT = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_BINARY_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BINARY_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BINARY_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BINARY_DATA_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/files/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FileSearchRepository fileSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private File file;

    private File insertedFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createEntity() {
        return new File()
            .folderId(DEFAULT_FOLDER_ID)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .fileSize(DEFAULT_FILE_SIZE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .modifiedAt(DEFAULT_MODIFIED_AT)
            .mimeType(DEFAULT_MIME_TYPE)
            .extension(DEFAULT_EXTENSION)
            .binaryData(DEFAULT_BINARY_DATA)
            .binaryDataContentType(DEFAULT_BINARY_DATA_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createUpdatedEntity() {
        return new File()
            .folderId(UPDATED_FOLDER_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .mimeType(UPDATED_MIME_TYPE)
            .extension(UPDATED_EXTENSION)
            .binaryData(UPDATED_BINARY_DATA)
            .binaryDataContentType(UPDATED_BINARY_DATA_CONTENT_TYPE);
    }

    @BeforeEach
    public void initTest() {
        file = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFile != null) {
            fileRepository.delete(insertedFile).block();
            fileSearchRepository.delete(insertedFile).block();
            insertedFile = null;
        }
    }

    @Test
    void createFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);
        var returnedFileDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fileDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FileDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the File in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFile = fileMapper.toEntity(returnedFileDTO);
        assertFileUpdatableFieldsEquals(returnedFile, getPersistedFile(returnedFile));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFile = returnedFile;
    }

    @Test
    void createFileWithExistingId() throws Exception {
        // Create the File with an existing ID
        file.setId("existing_id");
        FileDTO fileDTO = fileMapper.toDto(file);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFiles() {
        // Initialize the database
        insertedFile = fileRepository.save(file).block();

        // Get all the fileList
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
            .value(hasItem(file.getId()))
            .jsonPath("$.[*].folderId")
            .value(hasItem(DEFAULT_FOLDER_ID))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].fileSize")
            .value(hasItem(DEFAULT_FILE_SIZE))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].modifiedAt")
            .value(hasItem(DEFAULT_MODIFIED_AT))
            .jsonPath("$.[*].mimeType")
            .value(hasItem(DEFAULT_MIME_TYPE))
            .jsonPath("$.[*].extension")
            .value(hasItem(DEFAULT_EXTENSION))
            .jsonPath("$.[*].binaryDataContentType")
            .value(hasItem(DEFAULT_BINARY_DATA_CONTENT_TYPE))
            .jsonPath("$.[*].binaryData")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BINARY_DATA)));
    }

    @Test
    void getFile() {
        // Initialize the database
        insertedFile = fileRepository.save(file).block();

        // Get the file
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, file.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(file.getId()))
            .jsonPath("$.folderId")
            .value(is(DEFAULT_FOLDER_ID))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.fileSize")
            .value(is(DEFAULT_FILE_SIZE))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT))
            .jsonPath("$.modifiedAt")
            .value(is(DEFAULT_MODIFIED_AT))
            .jsonPath("$.mimeType")
            .value(is(DEFAULT_MIME_TYPE))
            .jsonPath("$.extension")
            .value(is(DEFAULT_EXTENSION))
            .jsonPath("$.binaryDataContentType")
            .value(is(DEFAULT_BINARY_DATA_CONTENT_TYPE))
            .jsonPath("$.binaryData")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BINARY_DATA)));
    }

    @Test
    void getNonExistingFile() {
        // Get the file
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFile() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.save(file).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        fileSearchRepository.save(file).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());

        // Update the file
        File updatedFile = fileRepository.findById(file.getId()).block();
        updatedFile
            .folderId(UPDATED_FOLDER_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .mimeType(UPDATED_MIME_TYPE)
            .extension(UPDATED_EXTENSION)
            .binaryData(UPDATED_BINARY_DATA)
            .binaryDataContentType(UPDATED_BINARY_DATA_CONTENT_TYPE);
        FileDTO fileDTO = fileMapper.toDto(updatedFile);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fileDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFileToMatchAllProperties(updatedFile);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<File> fileSearchList = Streamable.of(fileSearchRepository.findAll().collectList().block()).toList();
                File testFileSearch = fileSearchList.get(searchDatabaseSizeAfter - 1);

                assertFileAllPropertiesEquals(testFileSearch, updatedFile);
            });
    }

    @Test
    void putNonExistingFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        file.setId(UUID.randomUUID().toString());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        file.setId(UUID.randomUUID().toString());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        file.setId(UUID.randomUUID().toString());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(fileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFileWithPatch() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.save(file).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile.createdAt(UPDATED_CREATED_AT).modifiedAt(UPDATED_MODIFIED_AT).extension(UPDATED_EXTENSION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the File in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFile, file), getPersistedFile(file));
    }

    @Test
    void fullUpdateFileWithPatch() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.save(file).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile
            .folderId(UPDATED_FOLDER_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .mimeType(UPDATED_MIME_TYPE)
            .extension(UPDATED_EXTENSION)
            .binaryData(UPDATED_BINARY_DATA)
            .binaryDataContentType(UPDATED_BINARY_DATA_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the File in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileUpdatableFieldsEquals(partialUpdatedFile, getPersistedFile(partialUpdatedFile));
    }

    @Test
    void patchNonExistingFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        file.setId(UUID.randomUUID().toString());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, fileDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        file.setId(UUID.randomUUID().toString());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        file.setId(UUID.randomUUID().toString());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(fileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFile() {
        // Initialize the database
        insertedFile = fileRepository.save(file).block();
        fileRepository.save(file).block();
        fileSearchRepository.save(file).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the file
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, file.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFile() {
        // Initialize the database
        insertedFile = fileRepository.save(file).block();
        fileSearchRepository.save(file).block();

        // Search the file
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + file.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(file.getId()))
            .jsonPath("$.[*].folderId")
            .value(hasItem(DEFAULT_FOLDER_ID))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].fileSize")
            .value(hasItem(DEFAULT_FILE_SIZE))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].modifiedAt")
            .value(hasItem(DEFAULT_MODIFIED_AT))
            .jsonPath("$.[*].mimeType")
            .value(hasItem(DEFAULT_MIME_TYPE))
            .jsonPath("$.[*].extension")
            .value(hasItem(DEFAULT_EXTENSION))
            .jsonPath("$.[*].binaryDataContentType")
            .value(hasItem(DEFAULT_BINARY_DATA_CONTENT_TYPE))
            .jsonPath("$.[*].binaryData")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BINARY_DATA)));
    }

    protected long getRepositoryCount() {
        return fileRepository.count().block();
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

    protected File getPersistedFile(File file) {
        return fileRepository.findById(file.getId()).block();
    }

    protected void assertPersistedFileToMatchAllProperties(File expectedFile) {
        assertFileAllPropertiesEquals(expectedFile, getPersistedFile(expectedFile));
    }

    protected void assertPersistedFileToMatchUpdatableProperties(File expectedFile) {
        assertFileAllUpdatablePropertiesEquals(expectedFile, getPersistedFile(expectedFile));
    }
}
