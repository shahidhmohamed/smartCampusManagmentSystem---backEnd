package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.GroupChatMembersAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.GroupChatMembers;
import com.smartcampusmanagmentsystem.repository.GroupChatMembersRepository;
import com.smartcampusmanagmentsystem.repository.search.GroupChatMembersSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.GroupChatMembersDTO;
import com.smartcampusmanagmentsystem.service.mapper.GroupChatMembersMapper;
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
 * Integration tests for the {@link GroupChatMembersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GroupChatMembersResourceIT {

    private static final String DEFAULT_GROUP_CHAT_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_CHAT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MEMBER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MEMBER_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_USER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/group-chat-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/group-chat-members/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GroupChatMembersRepository groupChatMembersRepository;

    @Autowired
    private GroupChatMembersMapper groupChatMembersMapper;

    @Autowired
    private GroupChatMembersSearchRepository groupChatMembersSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private GroupChatMembers groupChatMembers;

    private GroupChatMembers insertedGroupChatMembers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupChatMembers createEntity() {
        return new GroupChatMembers()
            .groupChatId(DEFAULT_GROUP_CHAT_ID)
            .memberName(DEFAULT_MEMBER_NAME)
            .memberUserId(DEFAULT_MEMBER_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupChatMembers createUpdatedEntity() {
        return new GroupChatMembers()
            .groupChatId(UPDATED_GROUP_CHAT_ID)
            .memberName(UPDATED_MEMBER_NAME)
            .memberUserId(UPDATED_MEMBER_USER_ID);
    }

    @BeforeEach
    public void initTest() {
        groupChatMembers = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedGroupChatMembers != null) {
            groupChatMembersRepository.delete(insertedGroupChatMembers).block();
            groupChatMembersSearchRepository.delete(insertedGroupChatMembers).block();
            insertedGroupChatMembers = null;
        }
    }

    @Test
    void createGroupChatMembers() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        // Create the GroupChatMembers
        GroupChatMembersDTO groupChatMembersDTO = groupChatMembersMapper.toDto(groupChatMembers);
        var returnedGroupChatMembersDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatMembersDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(GroupChatMembersDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the GroupChatMembers in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGroupChatMembers = groupChatMembersMapper.toEntity(returnedGroupChatMembersDTO);
        assertGroupChatMembersUpdatableFieldsEquals(returnedGroupChatMembers, getPersistedGroupChatMembers(returnedGroupChatMembers));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedGroupChatMembers = returnedGroupChatMembers;
    }

    @Test
    void createGroupChatMembersWithExistingId() throws Exception {
        // Create the GroupChatMembers with an existing ID
        groupChatMembers.setId("existing_id");
        GroupChatMembersDTO groupChatMembersDTO = groupChatMembersMapper.toDto(groupChatMembers);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatMembersDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChatMembers in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllGroupChatMembers() {
        // Initialize the database
        insertedGroupChatMembers = groupChatMembersRepository.save(groupChatMembers).block();

        // Get all the groupChatMembersList
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
            .value(hasItem(groupChatMembers.getId()))
            .jsonPath("$.[*].groupChatId")
            .value(hasItem(DEFAULT_GROUP_CHAT_ID))
            .jsonPath("$.[*].memberName")
            .value(hasItem(DEFAULT_MEMBER_NAME))
            .jsonPath("$.[*].memberUserId")
            .value(hasItem(DEFAULT_MEMBER_USER_ID));
    }

    @Test
    void getGroupChatMembers() {
        // Initialize the database
        insertedGroupChatMembers = groupChatMembersRepository.save(groupChatMembers).block();

        // Get the groupChatMembers
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, groupChatMembers.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(groupChatMembers.getId()))
            .jsonPath("$.groupChatId")
            .value(is(DEFAULT_GROUP_CHAT_ID))
            .jsonPath("$.memberName")
            .value(is(DEFAULT_MEMBER_NAME))
            .jsonPath("$.memberUserId")
            .value(is(DEFAULT_MEMBER_USER_ID));
    }

    @Test
    void getNonExistingGroupChatMembers() {
        // Get the groupChatMembers
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingGroupChatMembers() throws Exception {
        // Initialize the database
        insertedGroupChatMembers = groupChatMembersRepository.save(groupChatMembers).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        groupChatMembersSearchRepository.save(groupChatMembers).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());

        // Update the groupChatMembers
        GroupChatMembers updatedGroupChatMembers = groupChatMembersRepository.findById(groupChatMembers.getId()).block();
        updatedGroupChatMembers.groupChatId(UPDATED_GROUP_CHAT_ID).memberName(UPDATED_MEMBER_NAME).memberUserId(UPDATED_MEMBER_USER_ID);
        GroupChatMembersDTO groupChatMembersDTO = groupChatMembersMapper.toDto(updatedGroupChatMembers);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, groupChatMembersDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatMembersDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GroupChatMembers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGroupChatMembersToMatchAllProperties(updatedGroupChatMembers);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<GroupChatMembers> groupChatMembersSearchList = Streamable.of(
                    groupChatMembersSearchRepository.findAll().collectList().block()
                ).toList();
                GroupChatMembers testGroupChatMembersSearch = groupChatMembersSearchList.get(searchDatabaseSizeAfter - 1);

                assertGroupChatMembersAllPropertiesEquals(testGroupChatMembersSearch, updatedGroupChatMembers);
            });
    }

    @Test
    void putNonExistingGroupChatMembers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        groupChatMembers.setId(UUID.randomUUID().toString());

        // Create the GroupChatMembers
        GroupChatMembersDTO groupChatMembersDTO = groupChatMembersMapper.toDto(groupChatMembers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, groupChatMembersDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatMembersDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChatMembers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchGroupChatMembers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        groupChatMembers.setId(UUID.randomUUID().toString());

        // Create the GroupChatMembers
        GroupChatMembersDTO groupChatMembersDTO = groupChatMembersMapper.toDto(groupChatMembers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatMembersDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChatMembers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamGroupChatMembers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        groupChatMembers.setId(UUID.randomUUID().toString());

        // Create the GroupChatMembers
        GroupChatMembersDTO groupChatMembersDTO = groupChatMembersMapper.toDto(groupChatMembers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatMembersDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GroupChatMembers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateGroupChatMembersWithPatch() throws Exception {
        // Initialize the database
        insertedGroupChatMembers = groupChatMembersRepository.save(groupChatMembers).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the groupChatMembers using partial update
        GroupChatMembers partialUpdatedGroupChatMembers = new GroupChatMembers();
        partialUpdatedGroupChatMembers.setId(groupChatMembers.getId());

        partialUpdatedGroupChatMembers.groupChatId(UPDATED_GROUP_CHAT_ID).memberUserId(UPDATED_MEMBER_USER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGroupChatMembers.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedGroupChatMembers))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GroupChatMembers in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGroupChatMembersUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGroupChatMembers, groupChatMembers),
            getPersistedGroupChatMembers(groupChatMembers)
        );
    }

    @Test
    void fullUpdateGroupChatMembersWithPatch() throws Exception {
        // Initialize the database
        insertedGroupChatMembers = groupChatMembersRepository.save(groupChatMembers).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the groupChatMembers using partial update
        GroupChatMembers partialUpdatedGroupChatMembers = new GroupChatMembers();
        partialUpdatedGroupChatMembers.setId(groupChatMembers.getId());

        partialUpdatedGroupChatMembers
            .groupChatId(UPDATED_GROUP_CHAT_ID)
            .memberName(UPDATED_MEMBER_NAME)
            .memberUserId(UPDATED_MEMBER_USER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGroupChatMembers.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedGroupChatMembers))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GroupChatMembers in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGroupChatMembersUpdatableFieldsEquals(
            partialUpdatedGroupChatMembers,
            getPersistedGroupChatMembers(partialUpdatedGroupChatMembers)
        );
    }

    @Test
    void patchNonExistingGroupChatMembers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        groupChatMembers.setId(UUID.randomUUID().toString());

        // Create the GroupChatMembers
        GroupChatMembersDTO groupChatMembersDTO = groupChatMembersMapper.toDto(groupChatMembers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, groupChatMembersDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(groupChatMembersDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChatMembers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchGroupChatMembers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        groupChatMembers.setId(UUID.randomUUID().toString());

        // Create the GroupChatMembers
        GroupChatMembersDTO groupChatMembersDTO = groupChatMembersMapper.toDto(groupChatMembers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(groupChatMembersDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChatMembers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamGroupChatMembers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        groupChatMembers.setId(UUID.randomUUID().toString());

        // Create the GroupChatMembers
        GroupChatMembersDTO groupChatMembersDTO = groupChatMembersMapper.toDto(groupChatMembers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(groupChatMembersDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GroupChatMembers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteGroupChatMembers() {
        // Initialize the database
        insertedGroupChatMembers = groupChatMembersRepository.save(groupChatMembers).block();
        groupChatMembersRepository.save(groupChatMembers).block();
        groupChatMembersSearchRepository.save(groupChatMembers).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the groupChatMembers
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, groupChatMembers.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatMembersSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchGroupChatMembers() {
        // Initialize the database
        insertedGroupChatMembers = groupChatMembersRepository.save(groupChatMembers).block();
        groupChatMembersSearchRepository.save(groupChatMembers).block();

        // Search the groupChatMembers
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + groupChatMembers.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(groupChatMembers.getId()))
            .jsonPath("$.[*].groupChatId")
            .value(hasItem(DEFAULT_GROUP_CHAT_ID))
            .jsonPath("$.[*].memberName")
            .value(hasItem(DEFAULT_MEMBER_NAME))
            .jsonPath("$.[*].memberUserId")
            .value(hasItem(DEFAULT_MEMBER_USER_ID));
    }

    protected long getRepositoryCount() {
        return groupChatMembersRepository.count().block();
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

    protected GroupChatMembers getPersistedGroupChatMembers(GroupChatMembers groupChatMembers) {
        return groupChatMembersRepository.findById(groupChatMembers.getId()).block();
    }

    protected void assertPersistedGroupChatMembersToMatchAllProperties(GroupChatMembers expectedGroupChatMembers) {
        assertGroupChatMembersAllPropertiesEquals(expectedGroupChatMembers, getPersistedGroupChatMembers(expectedGroupChatMembers));
    }

    protected void assertPersistedGroupChatMembersToMatchUpdatableProperties(GroupChatMembers expectedGroupChatMembers) {
        assertGroupChatMembersAllUpdatablePropertiesEquals(
            expectedGroupChatMembers,
            getPersistedGroupChatMembers(expectedGroupChatMembers)
        );
    }
}
