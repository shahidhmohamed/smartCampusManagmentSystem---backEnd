package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.ChatUserAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.ChatUser;
import com.smartcampusmanagmentsystem.repository.ChatUserRepository;
import com.smartcampusmanagmentsystem.repository.search.ChatUserSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ChatUserDTO;
import com.smartcampusmanagmentsystem.service.mapper.ChatUserMapper;
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
 * Integration tests for the {@link ChatUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ChatUserResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ABOUT = "AAAAAAAAAA";
    private static final String UPDATED_ABOUT = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTHDAY = "AAAAAAAAAA";
    private static final String UPDATED_BIRTHDAY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chat-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/chat-users/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private ChatUserMapper chatUserMapper;

    @Autowired
    private ChatUserSearchRepository chatUserSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ChatUser chatUser;

    private ChatUser insertedChatUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatUser createEntity() {
        return new ChatUser()
            .userId(DEFAULT_USER_ID)
            .avatar(DEFAULT_AVATAR)
            .name(DEFAULT_NAME)
            .about(DEFAULT_ABOUT)
            .title(DEFAULT_TITLE)
            .birthday(DEFAULT_BIRTHDAY)
            .address(DEFAULT_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatUser createUpdatedEntity() {
        return new ChatUser()
            .userId(UPDATED_USER_ID)
            .avatar(UPDATED_AVATAR)
            .name(UPDATED_NAME)
            .about(UPDATED_ABOUT)
            .title(UPDATED_TITLE)
            .birthday(UPDATED_BIRTHDAY)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        chatUser = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedChatUser != null) {
            chatUserRepository.delete(insertedChatUser).block();
            chatUserSearchRepository.delete(insertedChatUser).block();
            insertedChatUser = null;
        }
    }

    @Test
    void createChatUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);
        var returnedChatUserDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatUserDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ChatUserDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ChatUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChatUser = chatUserMapper.toEntity(returnedChatUserDTO);
        assertChatUserUpdatableFieldsEquals(returnedChatUser, getPersistedChatUser(returnedChatUser));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedChatUser = returnedChatUser;
    }

    @Test
    void createChatUserWithExistingId() throws Exception {
        // Create the ChatUser with an existing ID
        chatUser.setId("existing_id");
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllChatUsers() {
        // Initialize the database
        insertedChatUser = chatUserRepository.save(chatUser).block();

        // Get all the chatUserList
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
            .value(hasItem(chatUser.getId()))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].avatar")
            .value(hasItem(DEFAULT_AVATAR))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].about")
            .value(hasItem(DEFAULT_ABOUT))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].birthday")
            .value(hasItem(DEFAULT_BIRTHDAY))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS))
            .jsonPath("$.[*].phoneNumber")
            .value(hasItem(DEFAULT_PHONE_NUMBER));
    }

    @Test
    void getChatUser() {
        // Initialize the database
        insertedChatUser = chatUserRepository.save(chatUser).block();

        // Get the chatUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, chatUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(chatUser.getId()))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID))
            .jsonPath("$.avatar")
            .value(is(DEFAULT_AVATAR))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.about")
            .value(is(DEFAULT_ABOUT))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.birthday")
            .value(is(DEFAULT_BIRTHDAY))
            .jsonPath("$.address")
            .value(is(DEFAULT_ADDRESS))
            .jsonPath("$.phoneNumber")
            .value(is(DEFAULT_PHONE_NUMBER));
    }

    @Test
    void getNonExistingChatUser() {
        // Get the chatUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingChatUser() throws Exception {
        // Initialize the database
        insertedChatUser = chatUserRepository.save(chatUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatUserSearchRepository.save(chatUser).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());

        // Update the chatUser
        ChatUser updatedChatUser = chatUserRepository.findById(chatUser.getId()).block();
        updatedChatUser
            .userId(UPDATED_USER_ID)
            .avatar(UPDATED_AVATAR)
            .name(UPDATED_NAME)
            .about(UPDATED_ABOUT)
            .title(UPDATED_TITLE)
            .birthday(UPDATED_BIRTHDAY)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(updatedChatUser);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chatUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatUserDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChatUserToMatchAllProperties(updatedChatUser);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ChatUser> chatUserSearchList = Streamable.of(chatUserSearchRepository.findAll().collectList().block()).toList();
                ChatUser testChatUserSearch = chatUserSearchList.get(searchDatabaseSizeAfter - 1);

                assertChatUserAllPropertiesEquals(testChatUserSearch, updatedChatUser);
            });
    }

    @Test
    void putNonExistingChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        chatUser.setId(UUID.randomUUID().toString());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chatUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        chatUser.setId(UUID.randomUUID().toString());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        chatUser.setId(UUID.randomUUID().toString());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateChatUserWithPatch() throws Exception {
        // Initialize the database
        insertedChatUser = chatUserRepository.save(chatUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatUser using partial update
        ChatUser partialUpdatedChatUser = new ChatUser();
        partialUpdatedChatUser.setId(chatUser.getId());

        partialUpdatedChatUser.avatar(UPDATED_AVATAR).title(UPDATED_TITLE).birthday(UPDATED_BIRTHDAY).phoneNumber(UPDATED_PHONE_NUMBER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChatUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedChatUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChatUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedChatUser, chatUser), getPersistedChatUser(chatUser));
    }

    @Test
    void fullUpdateChatUserWithPatch() throws Exception {
        // Initialize the database
        insertedChatUser = chatUserRepository.save(chatUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatUser using partial update
        ChatUser partialUpdatedChatUser = new ChatUser();
        partialUpdatedChatUser.setId(chatUser.getId());

        partialUpdatedChatUser
            .userId(UPDATED_USER_ID)
            .avatar(UPDATED_AVATAR)
            .name(UPDATED_NAME)
            .about(UPDATED_ABOUT)
            .title(UPDATED_TITLE)
            .birthday(UPDATED_BIRTHDAY)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChatUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedChatUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChatUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatUserUpdatableFieldsEquals(partialUpdatedChatUser, getPersistedChatUser(partialUpdatedChatUser));
    }

    @Test
    void patchNonExistingChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        chatUser.setId(UUID.randomUUID().toString());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, chatUserDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(chatUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        chatUser.setId(UUID.randomUUID().toString());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(chatUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        chatUser.setId(UUID.randomUUID().toString());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(chatUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteChatUser() {
        // Initialize the database
        insertedChatUser = chatUserRepository.save(chatUser).block();
        chatUserRepository.save(chatUser).block();
        chatUserSearchRepository.save(chatUser).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the chatUser
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, chatUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchChatUser() {
        // Initialize the database
        insertedChatUser = chatUserRepository.save(chatUser).block();
        chatUserSearchRepository.save(chatUser).block();

        // Search the chatUser
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + chatUser.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(chatUser.getId()))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].avatar")
            .value(hasItem(DEFAULT_AVATAR))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].about")
            .value(hasItem(DEFAULT_ABOUT))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].birthday")
            .value(hasItem(DEFAULT_BIRTHDAY))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS))
            .jsonPath("$.[*].phoneNumber")
            .value(hasItem(DEFAULT_PHONE_NUMBER));
    }

    protected long getRepositoryCount() {
        return chatUserRepository.count().block();
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

    protected ChatUser getPersistedChatUser(ChatUser chatUser) {
        return chatUserRepository.findById(chatUser.getId()).block();
    }

    protected void assertPersistedChatUserToMatchAllProperties(ChatUser expectedChatUser) {
        assertChatUserAllPropertiesEquals(expectedChatUser, getPersistedChatUser(expectedChatUser));
    }

    protected void assertPersistedChatUserToMatchUpdatableProperties(ChatUser expectedChatUser) {
        assertChatUserAllUpdatablePropertiesEquals(expectedChatUser, getPersistedChatUser(expectedChatUser));
    }
}
