package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.AssignmentFileAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.AssignmentFile;
import com.smartcampusmanagmentsystem.domain.enumeration.MarkingStatus;
import com.smartcampusmanagmentsystem.repository.AssignmentFileRepository;
import com.smartcampusmanagmentsystem.repository.search.AssignmentFileSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.AssignmentFileDTO;
import com.smartcampusmanagmentsystem.service.mapper.AssignmentFileMapper;
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
 * Integration tests for the {@link AssignmentFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AssignmentFileResourceIT {

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNMENT_ID = "BBBBBBBBBB";

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

    private static final MarkingStatus DEFAULT_MARKING_STATUS = MarkingStatus.PENDING;
    private static final MarkingStatus UPDATED_MARKING_STATUS = MarkingStatus.REVIEWED;

    private static final Float DEFAULT_GRADE = 1F;
    private static final Float UPDATED_GRADE = 2F;

    private static final String DEFAULT_FEEDBACK = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK = "BBBBBBBBBB";

    private static final String DEFAULT_GRADED_BY = "AAAAAAAAAA";
    private static final String UPDATED_GRADED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_GRADED_AT = "AAAAAAAAAA";
    private static final String UPDATED_GRADED_AT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SUBMITTED = false;
    private static final Boolean UPDATED_IS_SUBMITTED = true;

    private static final String ENTITY_API_URL = "/api/assignment-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/assignment-files/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AssignmentFileRepository assignmentFileRepository;

    @Autowired
    private AssignmentFileMapper assignmentFileMapper;

    @Autowired
    private AssignmentFileSearchRepository assignmentFileSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private AssignmentFile assignmentFile;

    private AssignmentFile insertedAssignmentFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssignmentFile createEntity() {
        return new AssignmentFile()
            .studentId(DEFAULT_STUDENT_ID)
            .assignmentId(DEFAULT_ASSIGNMENT_ID)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .fileSize(DEFAULT_FILE_SIZE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .modifiedAt(DEFAULT_MODIFIED_AT)
            .mimeType(DEFAULT_MIME_TYPE)
            .extension(DEFAULT_EXTENSION)
            .binaryData(DEFAULT_BINARY_DATA)
            .binaryDataContentType(DEFAULT_BINARY_DATA_CONTENT_TYPE)
            .markingStatus(DEFAULT_MARKING_STATUS)
            .grade(DEFAULT_GRADE)
            .feedback(DEFAULT_FEEDBACK)
            .gradedBy(DEFAULT_GRADED_BY)
            .gradedAt(DEFAULT_GRADED_AT)
            .isSubmitted(DEFAULT_IS_SUBMITTED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssignmentFile createUpdatedEntity() {
        return new AssignmentFile()
            .studentId(UPDATED_STUDENT_ID)
            .assignmentId(UPDATED_ASSIGNMENT_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .mimeType(UPDATED_MIME_TYPE)
            .extension(UPDATED_EXTENSION)
            .binaryData(UPDATED_BINARY_DATA)
            .binaryDataContentType(UPDATED_BINARY_DATA_CONTENT_TYPE)
            .markingStatus(UPDATED_MARKING_STATUS)
            .grade(UPDATED_GRADE)
            .feedback(UPDATED_FEEDBACK)
            .gradedBy(UPDATED_GRADED_BY)
            .gradedAt(UPDATED_GRADED_AT)
            .isSubmitted(UPDATED_IS_SUBMITTED);
    }

    @BeforeEach
    public void initTest() {
        assignmentFile = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAssignmentFile != null) {
            assignmentFileRepository.delete(insertedAssignmentFile).block();
            assignmentFileSearchRepository.delete(insertedAssignmentFile).block();
            insertedAssignmentFile = null;
        }
    }

    @Test
    void createAssignmentFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        // Create the AssignmentFile
        AssignmentFileDTO assignmentFileDTO = assignmentFileMapper.toDto(assignmentFile);
        var returnedAssignmentFileDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(assignmentFileDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AssignmentFileDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the AssignmentFile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAssignmentFile = assignmentFileMapper.toEntity(returnedAssignmentFileDTO);
        assertAssignmentFileUpdatableFieldsEquals(returnedAssignmentFile, getPersistedAssignmentFile(returnedAssignmentFile));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAssignmentFile = returnedAssignmentFile;
    }

    @Test
    void createAssignmentFileWithExistingId() throws Exception {
        // Create the AssignmentFile with an existing ID
        assignmentFile.setId("existing_id");
        AssignmentFileDTO assignmentFileDTO = assignmentFileMapper.toDto(assignmentFile);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(assignmentFileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssignmentFile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAssignmentFiles() {
        // Initialize the database
        insertedAssignmentFile = assignmentFileRepository.save(assignmentFile).block();

        // Get all the assignmentFileList
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
            .value(hasItem(assignmentFile.getId()))
            .jsonPath("$.[*].studentId")
            .value(hasItem(DEFAULT_STUDENT_ID))
            .jsonPath("$.[*].assignmentId")
            .value(hasItem(DEFAULT_ASSIGNMENT_ID))
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
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BINARY_DATA)))
            .jsonPath("$.[*].markingStatus")
            .value(hasItem(DEFAULT_MARKING_STATUS.toString()))
            .jsonPath("$.[*].grade")
            .value(hasItem(DEFAULT_GRADE.doubleValue()))
            .jsonPath("$.[*].feedback")
            .value(hasItem(DEFAULT_FEEDBACK))
            .jsonPath("$.[*].gradedBy")
            .value(hasItem(DEFAULT_GRADED_BY))
            .jsonPath("$.[*].gradedAt")
            .value(hasItem(DEFAULT_GRADED_AT))
            .jsonPath("$.[*].isSubmitted")
            .value(hasItem(DEFAULT_IS_SUBMITTED));
    }

    @Test
    void getAssignmentFile() {
        // Initialize the database
        insertedAssignmentFile = assignmentFileRepository.save(assignmentFile).block();

        // Get the assignmentFile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, assignmentFile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(assignmentFile.getId()))
            .jsonPath("$.studentId")
            .value(is(DEFAULT_STUDENT_ID))
            .jsonPath("$.assignmentId")
            .value(is(DEFAULT_ASSIGNMENT_ID))
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
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BINARY_DATA)))
            .jsonPath("$.markingStatus")
            .value(is(DEFAULT_MARKING_STATUS.toString()))
            .jsonPath("$.grade")
            .value(is(DEFAULT_GRADE.doubleValue()))
            .jsonPath("$.feedback")
            .value(is(DEFAULT_FEEDBACK))
            .jsonPath("$.gradedBy")
            .value(is(DEFAULT_GRADED_BY))
            .jsonPath("$.gradedAt")
            .value(is(DEFAULT_GRADED_AT))
            .jsonPath("$.isSubmitted")
            .value(is(DEFAULT_IS_SUBMITTED));
    }

    @Test
    void getNonExistingAssignmentFile() {
        // Get the assignmentFile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAssignmentFile() throws Exception {
        // Initialize the database
        insertedAssignmentFile = assignmentFileRepository.save(assignmentFile).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        assignmentFileSearchRepository.save(assignmentFile).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());

        // Update the assignmentFile
        AssignmentFile updatedAssignmentFile = assignmentFileRepository.findById(assignmentFile.getId()).block();
        updatedAssignmentFile
            .studentId(UPDATED_STUDENT_ID)
            .assignmentId(UPDATED_ASSIGNMENT_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .mimeType(UPDATED_MIME_TYPE)
            .extension(UPDATED_EXTENSION)
            .binaryData(UPDATED_BINARY_DATA)
            .binaryDataContentType(UPDATED_BINARY_DATA_CONTENT_TYPE)
            .markingStatus(UPDATED_MARKING_STATUS)
            .grade(UPDATED_GRADE)
            .feedback(UPDATED_FEEDBACK)
            .gradedBy(UPDATED_GRADED_BY)
            .gradedAt(UPDATED_GRADED_AT)
            .isSubmitted(UPDATED_IS_SUBMITTED);
        AssignmentFileDTO assignmentFileDTO = assignmentFileMapper.toDto(updatedAssignmentFile);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, assignmentFileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(assignmentFileDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AssignmentFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAssignmentFileToMatchAllProperties(updatedAssignmentFile);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AssignmentFile> assignmentFileSearchList = Streamable.of(
                    assignmentFileSearchRepository.findAll().collectList().block()
                ).toList();
                AssignmentFile testAssignmentFileSearch = assignmentFileSearchList.get(searchDatabaseSizeAfter - 1);

                assertAssignmentFileAllPropertiesEquals(testAssignmentFileSearch, updatedAssignmentFile);
            });
    }

    @Test
    void putNonExistingAssignmentFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assignmentFile.setId(UUID.randomUUID().toString());

        // Create the AssignmentFile
        AssignmentFileDTO assignmentFileDTO = assignmentFileMapper.toDto(assignmentFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, assignmentFileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(assignmentFileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssignmentFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAssignmentFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assignmentFile.setId(UUID.randomUUID().toString());

        // Create the AssignmentFile
        AssignmentFileDTO assignmentFileDTO = assignmentFileMapper.toDto(assignmentFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(assignmentFileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssignmentFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAssignmentFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assignmentFile.setId(UUID.randomUUID().toString());

        // Create the AssignmentFile
        AssignmentFileDTO assignmentFileDTO = assignmentFileMapper.toDto(assignmentFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(assignmentFileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AssignmentFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAssignmentFileWithPatch() throws Exception {
        // Initialize the database
        insertedAssignmentFile = assignmentFileRepository.save(assignmentFile).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assignmentFile using partial update
        AssignmentFile partialUpdatedAssignmentFile = new AssignmentFile();
        partialUpdatedAssignmentFile.setId(assignmentFile.getId());

        partialUpdatedAssignmentFile
            .studentId(UPDATED_STUDENT_ID)
            .type(UPDATED_TYPE)
            .mimeType(UPDATED_MIME_TYPE)
            .binaryData(UPDATED_BINARY_DATA)
            .binaryDataContentType(UPDATED_BINARY_DATA_CONTENT_TYPE)
            .isSubmitted(UPDATED_IS_SUBMITTED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssignmentFile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAssignmentFile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AssignmentFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssignmentFileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAssignmentFile, assignmentFile),
            getPersistedAssignmentFile(assignmentFile)
        );
    }

    @Test
    void fullUpdateAssignmentFileWithPatch() throws Exception {
        // Initialize the database
        insertedAssignmentFile = assignmentFileRepository.save(assignmentFile).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assignmentFile using partial update
        AssignmentFile partialUpdatedAssignmentFile = new AssignmentFile();
        partialUpdatedAssignmentFile.setId(assignmentFile.getId());

        partialUpdatedAssignmentFile
            .studentId(UPDATED_STUDENT_ID)
            .assignmentId(UPDATED_ASSIGNMENT_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .mimeType(UPDATED_MIME_TYPE)
            .extension(UPDATED_EXTENSION)
            .binaryData(UPDATED_BINARY_DATA)
            .binaryDataContentType(UPDATED_BINARY_DATA_CONTENT_TYPE)
            .markingStatus(UPDATED_MARKING_STATUS)
            .grade(UPDATED_GRADE)
            .feedback(UPDATED_FEEDBACK)
            .gradedBy(UPDATED_GRADED_BY)
            .gradedAt(UPDATED_GRADED_AT)
            .isSubmitted(UPDATED_IS_SUBMITTED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssignmentFile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAssignmentFile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AssignmentFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssignmentFileUpdatableFieldsEquals(partialUpdatedAssignmentFile, getPersistedAssignmentFile(partialUpdatedAssignmentFile));
    }

    @Test
    void patchNonExistingAssignmentFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assignmentFile.setId(UUID.randomUUID().toString());

        // Create the AssignmentFile
        AssignmentFileDTO assignmentFileDTO = assignmentFileMapper.toDto(assignmentFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, assignmentFileDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(assignmentFileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssignmentFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAssignmentFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assignmentFile.setId(UUID.randomUUID().toString());

        // Create the AssignmentFile
        AssignmentFileDTO assignmentFileDTO = assignmentFileMapper.toDto(assignmentFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(assignmentFileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssignmentFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAssignmentFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assignmentFile.setId(UUID.randomUUID().toString());

        // Create the AssignmentFile
        AssignmentFileDTO assignmentFileDTO = assignmentFileMapper.toDto(assignmentFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(assignmentFileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AssignmentFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAssignmentFile() {
        // Initialize the database
        insertedAssignmentFile = assignmentFileRepository.save(assignmentFile).block();
        assignmentFileRepository.save(assignmentFile).block();
        assignmentFileSearchRepository.save(assignmentFile).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the assignmentFile
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, assignmentFile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentFileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAssignmentFile() {
        // Initialize the database
        insertedAssignmentFile = assignmentFileRepository.save(assignmentFile).block();
        assignmentFileSearchRepository.save(assignmentFile).block();

        // Search the assignmentFile
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + assignmentFile.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(assignmentFile.getId()))
            .jsonPath("$.[*].studentId")
            .value(hasItem(DEFAULT_STUDENT_ID))
            .jsonPath("$.[*].assignmentId")
            .value(hasItem(DEFAULT_ASSIGNMENT_ID))
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
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BINARY_DATA)))
            .jsonPath("$.[*].markingStatus")
            .value(hasItem(DEFAULT_MARKING_STATUS.toString()))
            .jsonPath("$.[*].grade")
            .value(hasItem(DEFAULT_GRADE.doubleValue()))
            .jsonPath("$.[*].feedback")
            .value(hasItem(DEFAULT_FEEDBACK))
            .jsonPath("$.[*].gradedBy")
            .value(hasItem(DEFAULT_GRADED_BY))
            .jsonPath("$.[*].gradedAt")
            .value(hasItem(DEFAULT_GRADED_AT))
            .jsonPath("$.[*].isSubmitted")
            .value(hasItem(DEFAULT_IS_SUBMITTED));
    }

    protected long getRepositoryCount() {
        return assignmentFileRepository.count().block();
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

    protected AssignmentFile getPersistedAssignmentFile(AssignmentFile assignmentFile) {
        return assignmentFileRepository.findById(assignmentFile.getId()).block();
    }

    protected void assertPersistedAssignmentFileToMatchAllProperties(AssignmentFile expectedAssignmentFile) {
        assertAssignmentFileAllPropertiesEquals(expectedAssignmentFile, getPersistedAssignmentFile(expectedAssignmentFile));
    }

    protected void assertPersistedAssignmentFileToMatchUpdatableProperties(AssignmentFile expectedAssignmentFile) {
        assertAssignmentFileAllUpdatablePropertiesEquals(expectedAssignmentFile, getPersistedAssignmentFile(expectedAssignmentFile));
    }
}
