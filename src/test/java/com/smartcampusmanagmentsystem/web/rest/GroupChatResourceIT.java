package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.GroupChatAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.GroupChat;
import com.smartcampusmanagmentsystem.repository.GroupChatRepository;
import com.smartcampusmanagmentsystem.repository.search.GroupChatSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.GroupChatDTO;
import com.smartcampusmanagmentsystem.service.mapper.GroupChatMapper;
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
 * Integration tests for the {@link GroupChatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GroupChatResourceIT {

    private static final String DEFAULT_UNREAD_COUNT = "AAAAAAAAAA";
    private static final String UPDATED_UNREAD_COUNT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MUTED = false;
    private static final Boolean UPDATED_MUTED = true;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_AT = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/group-chats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/group-chats/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private GroupChatMapper groupChatMapper;

    @Autowired
    private GroupChatSearchRepository groupChatSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private GroupChat groupChat;

    private GroupChat insertedGroupChat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupChat createEntity() {
        return new GroupChat()
            .unreadCount(DEFAULT_UNREAD_COUNT)
            .muted(DEFAULT_MUTED)
            .title(DEFAULT_TITLE)
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .owner(DEFAULT_OWNER)
            .ownerName(DEFAULT_OWNER_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupChat createUpdatedEntity() {
        return new GroupChat()
            .unreadCount(UPDATED_UNREAD_COUNT)
            .muted(UPDATED_MUTED)
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .owner(UPDATED_OWNER)
            .ownerName(UPDATED_OWNER_NAME);
    }

    @BeforeEach
    public void initTest() {
        groupChat = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedGroupChat != null) {
            groupChatRepository.delete(insertedGroupChat).block();
            groupChatSearchRepository.delete(insertedGroupChat).block();
            insertedGroupChat = null;
        }
    }

    @Test
    void createGroupChat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);
        var returnedGroupChatDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(GroupChatDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the GroupChat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGroupChat = groupChatMapper.toEntity(returnedGroupChatDTO);
        assertGroupChatUpdatableFieldsEquals(returnedGroupChat, getPersistedGroupChat(returnedGroupChat));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedGroupChat = returnedGroupChat;
    }

    @Test
    void createGroupChatWithExistingId() throws Exception {
        // Create the GroupChat with an existing ID
        groupChat.setId("existing_id");
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllGroupChats() {
        // Initialize the database
        insertedGroupChat = groupChatRepository.save(groupChat).block();

        // Get all the groupChatList
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
            .value(hasItem(groupChat.getId()))
            .jsonPath("$.[*].unreadCount")
            .value(hasItem(DEFAULT_UNREAD_COUNT))
            .jsonPath("$.[*].muted")
            .value(hasItem(DEFAULT_MUTED))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].owner")
            .value(hasItem(DEFAULT_OWNER))
            .jsonPath("$.[*].ownerName")
            .value(hasItem(DEFAULT_OWNER_NAME));
    }

    @Test
    void getGroupChat() {
        // Initialize the database
        insertedGroupChat = groupChatRepository.save(groupChat).block();

        // Get the groupChat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, groupChat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(groupChat.getId()))
            .jsonPath("$.unreadCount")
            .value(is(DEFAULT_UNREAD_COUNT))
            .jsonPath("$.muted")
            .value(is(DEFAULT_MUTED))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT))
            .jsonPath("$.owner")
            .value(is(DEFAULT_OWNER))
            .jsonPath("$.ownerName")
            .value(is(DEFAULT_OWNER_NAME));
    }

    @Test
    void getNonExistingGroupChat() {
        // Get the groupChat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingGroupChat() throws Exception {
        // Initialize the database
        insertedGroupChat = groupChatRepository.save(groupChat).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        groupChatSearchRepository.save(groupChat).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());

        // Update the groupChat
        GroupChat updatedGroupChat = groupChatRepository.findById(groupChat.getId()).block();
        updatedGroupChat
            .unreadCount(UPDATED_UNREAD_COUNT)
            .muted(UPDATED_MUTED)
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .owner(UPDATED_OWNER)
            .ownerName(UPDATED_OWNER_NAME);
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(updatedGroupChat);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, groupChatDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GroupChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGroupChatToMatchAllProperties(updatedGroupChat);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<GroupChat> groupChatSearchList = Streamable.of(groupChatSearchRepository.findAll().collectList().block()).toList();
                GroupChat testGroupChatSearch = groupChatSearchList.get(searchDatabaseSizeAfter - 1);

                assertGroupChatAllPropertiesEquals(testGroupChatSearch, updatedGroupChat);
            });
    }

    @Test
    void putNonExistingGroupChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, groupChatDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchGroupChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamGroupChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(groupChatDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GroupChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateGroupChatWithPatch() throws Exception {
        // Initialize the database
        insertedGroupChat = groupChatRepository.save(groupChat).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the groupChat using partial update
        GroupChat partialUpdatedGroupChat = new GroupChat();
        partialUpdatedGroupChat.setId(groupChat.getId());

        partialUpdatedGroupChat.unreadCount(UPDATED_UNREAD_COUNT).muted(UPDATED_MUTED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGroupChat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedGroupChat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GroupChat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGroupChatUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGroupChat, groupChat),
            getPersistedGroupChat(groupChat)
        );
    }

    @Test
    void fullUpdateGroupChatWithPatch() throws Exception {
        // Initialize the database
        insertedGroupChat = groupChatRepository.save(groupChat).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the groupChat using partial update
        GroupChat partialUpdatedGroupChat = new GroupChat();
        partialUpdatedGroupChat.setId(groupChat.getId());

        partialUpdatedGroupChat
            .unreadCount(UPDATED_UNREAD_COUNT)
            .muted(UPDATED_MUTED)
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .owner(UPDATED_OWNER)
            .ownerName(UPDATED_OWNER_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGroupChat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedGroupChat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GroupChat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGroupChatUpdatableFieldsEquals(partialUpdatedGroupChat, getPersistedGroupChat(partialUpdatedGroupChat));
    }

    @Test
    void patchNonExistingGroupChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, groupChatDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(groupChatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchGroupChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(groupChatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamGroupChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        groupChat.setId(UUID.randomUUID().toString());

        // Create the GroupChat
        GroupChatDTO groupChatDTO = groupChatMapper.toDto(groupChat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(groupChatDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GroupChat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteGroupChat() {
        // Initialize the database
        insertedGroupChat = groupChatRepository.save(groupChat).block();
        groupChatRepository.save(groupChat).block();
        groupChatSearchRepository.save(groupChat).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the groupChat
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, groupChat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(groupChatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchGroupChat() {
        // Initialize the database
        insertedGroupChat = groupChatRepository.save(groupChat).block();
        groupChatSearchRepository.save(groupChat).block();

        // Search the groupChat
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + groupChat.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(groupChat.getId()))
            .jsonPath("$.[*].unreadCount")
            .value(hasItem(DEFAULT_UNREAD_COUNT))
            .jsonPath("$.[*].muted")
            .value(hasItem(DEFAULT_MUTED))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].owner")
            .value(hasItem(DEFAULT_OWNER))
            .jsonPath("$.[*].ownerName")
            .value(hasItem(DEFAULT_OWNER_NAME));
    }

    protected long getRepositoryCount() {
        return groupChatRepository.count().block();
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

    protected GroupChat getPersistedGroupChat(GroupChat groupChat) {
        return groupChatRepository.findById(groupChat.getId()).block();
    }

    protected void assertPersistedGroupChatToMatchAllProperties(GroupChat expectedGroupChat) {
        assertGroupChatAllPropertiesEquals(expectedGroupChat, getPersistedGroupChat(expectedGroupChat));
    }

    protected void assertPersistedGroupChatToMatchUpdatableProperties(GroupChat expectedGroupChat) {
        assertGroupChatAllUpdatablePropertiesEquals(expectedGroupChat, getPersistedGroupChat(expectedGroupChat));
    }
}
