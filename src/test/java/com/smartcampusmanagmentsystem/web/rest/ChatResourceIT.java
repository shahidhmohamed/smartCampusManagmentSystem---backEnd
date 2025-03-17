package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.ChatAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.Chat;
import com.smartcampusmanagmentsystem.repository.ChatRepository;
import com.smartcampusmanagmentsystem.repository.search.ChatSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.ChatDTO;
import com.smartcampusmanagmentsystem.service.mapper.ChatMapper;
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
 * Integration tests for the {@link ChatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ChatResourceIT {

    private static final String DEFAULT_CONTACT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

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

    private static final String DEFAULT_BINARY_DATA = "AAAAAAAAAA";
    private static final String UPDATED_BINARY_DATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/chats/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ChatSearchRepository chatSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Chat chat;

    private Chat insertedChat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chat createEntity() {
        return new Chat()
            .contactId(DEFAULT_CONTACT_ID)
            .contact(DEFAULT_CONTACT)
            .unreadCount(DEFAULT_UNREAD_COUNT)
            .muted(DEFAULT_MUTED)
            .title(DEFAULT_TITLE)
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .owner(DEFAULT_OWNER)
            .ownerName(DEFAULT_OWNER_NAME)
            .binaryData(DEFAULT_BINARY_DATA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chat createUpdatedEntity() {
        return new Chat()
            .contactId(UPDATED_CONTACT_ID)
            .contact(UPDATED_CONTACT)
            .unreadCount(UPDATED_UNREAD_COUNT)
            .muted(UPDATED_MUTED)
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .owner(UPDATED_OWNER)
            .ownerName(UPDATED_OWNER_NAME)
            .binaryData(UPDATED_BINARY_DATA);
    }

    @BeforeEach
    public void initTest() {
        chat = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedChat != null) {
            chatRepository.delete(insertedChat).block();
            chatSearchRepository.delete(insertedChat).block();
            insertedChat = null;
        }
    }

    @Test
    void createChat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);
        var returnedChatDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ChatDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Chat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChat = chatMapper.toEntity(returnedChatDTO);
        assertChatUpdatableFieldsEquals(returnedChat, getPersistedChat(returnedChat));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedChat = returnedChat;
    }

    @Test
    void createChatWithExistingId() throws Exception {
        // Create the Chat with an existing ID
        chat.setId("existing_id");
        ChatDTO chatDTO = chatMapper.toDto(chat);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllChats() {
        // Initialize the database
        insertedChat = chatRepository.save(chat).block();

        // Get all the chatList
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
            .value(hasItem(chat.getId()))
            .jsonPath("$.[*].contactId")
            .value(hasItem(DEFAULT_CONTACT_ID))
            .jsonPath("$.[*].contact")
            .value(hasItem(DEFAULT_CONTACT))
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
            .value(hasItem(DEFAULT_OWNER_NAME))
            .jsonPath("$.[*].binaryData")
            .value(hasItem(DEFAULT_BINARY_DATA));
    }

    @Test
    void getChat() {
        // Initialize the database
        insertedChat = chatRepository.save(chat).block();

        // Get the chat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, chat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(chat.getId()))
            .jsonPath("$.contactId")
            .value(is(DEFAULT_CONTACT_ID))
            .jsonPath("$.contact")
            .value(is(DEFAULT_CONTACT))
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
            .value(is(DEFAULT_OWNER_NAME))
            .jsonPath("$.binaryData")
            .value(is(DEFAULT_BINARY_DATA));
    }

    @Test
    void getNonExistingChat() {
        // Get the chat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingChat() throws Exception {
        // Initialize the database
        insertedChat = chatRepository.save(chat).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatSearchRepository.save(chat).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());

        // Update the chat
        Chat updatedChat = chatRepository.findById(chat.getId()).block();
        updatedChat
            .contactId(UPDATED_CONTACT_ID)
            .contact(UPDATED_CONTACT)
            .unreadCount(UPDATED_UNREAD_COUNT)
            .muted(UPDATED_MUTED)
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .owner(UPDATED_OWNER)
            .ownerName(UPDATED_OWNER_NAME)
            .binaryData(UPDATED_BINARY_DATA);
        ChatDTO chatDTO = chatMapper.toDto(updatedChat);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chatDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Chat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChatToMatchAllProperties(updatedChat);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Chat> chatSearchList = Streamable.of(chatSearchRepository.findAll().collectList().block()).toList();
                Chat testChatSearch = chatSearchList.get(searchDatabaseSizeAfter - 1);

                assertChatAllPropertiesEquals(testChatSearch, updatedChat);
            });
    }

    @Test
    void putNonExistingChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        chat.setId(UUID.randomUUID().toString());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chatDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        chat.setId(UUID.randomUUID().toString());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        chat.setId(UUID.randomUUID().toString());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(chatDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Chat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateChatWithPatch() throws Exception {
        // Initialize the database
        insertedChat = chatRepository.save(chat).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chat using partial update
        Chat partialUpdatedChat = new Chat();
        partialUpdatedChat.setId(chat.getId());

        partialUpdatedChat
            .contactId(UPDATED_CONTACT_ID)
            .contact(UPDATED_CONTACT)
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .owner(UPDATED_OWNER)
            .binaryData(UPDATED_BINARY_DATA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedChat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Chat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedChat, chat), getPersistedChat(chat));
    }

    @Test
    void fullUpdateChatWithPatch() throws Exception {
        // Initialize the database
        insertedChat = chatRepository.save(chat).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chat using partial update
        Chat partialUpdatedChat = new Chat();
        partialUpdatedChat.setId(chat.getId());

        partialUpdatedChat
            .contactId(UPDATED_CONTACT_ID)
            .contact(UPDATED_CONTACT)
            .unreadCount(UPDATED_UNREAD_COUNT)
            .muted(UPDATED_MUTED)
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .owner(UPDATED_OWNER)
            .ownerName(UPDATED_OWNER_NAME)
            .binaryData(UPDATED_BINARY_DATA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedChat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Chat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatUpdatableFieldsEquals(partialUpdatedChat, getPersistedChat(partialUpdatedChat));
    }

    @Test
    void patchNonExistingChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        chat.setId(UUID.randomUUID().toString());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, chatDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(chatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        chat.setId(UUID.randomUUID().toString());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(chatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Chat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamChat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        chat.setId(UUID.randomUUID().toString());

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(chatDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Chat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteChat() {
        // Initialize the database
        insertedChat = chatRepository.save(chat).block();
        chatRepository.save(chat).block();
        chatSearchRepository.save(chat).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the chat
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, chat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchChat() {
        // Initialize the database
        insertedChat = chatRepository.save(chat).block();
        chatSearchRepository.save(chat).block();

        // Search the chat
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + chat.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(chat.getId()))
            .jsonPath("$.[*].contactId")
            .value(hasItem(DEFAULT_CONTACT_ID))
            .jsonPath("$.[*].contact")
            .value(hasItem(DEFAULT_CONTACT))
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
            .value(hasItem(DEFAULT_OWNER_NAME))
            .jsonPath("$.[*].binaryData")
            .value(hasItem(DEFAULT_BINARY_DATA));
    }

    protected long getRepositoryCount() {
        return chatRepository.count().block();
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

    protected Chat getPersistedChat(Chat chat) {
        return chatRepository.findById(chat.getId()).block();
    }

    protected void assertPersistedChatToMatchAllProperties(Chat expectedChat) {
        assertChatAllPropertiesEquals(expectedChat, getPersistedChat(expectedChat));
    }

    protected void assertPersistedChatToMatchUpdatableProperties(Chat expectedChat) {
        assertChatAllUpdatablePropertiesEquals(expectedChat, getPersistedChat(expectedChat));
    }
}
