package com.smartcampusmanagmentsystem.web.rest;

import static com.smartcampusmanagmentsystem.domain.MessageAsserts.*;
import static com.smartcampusmanagmentsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.Message;
import com.smartcampusmanagmentsystem.repository.MessageRepository;
import com.smartcampusmanagmentsystem.repository.search.MessageSearchRepository;
import com.smartcampusmanagmentsystem.service.dto.MessageDTO;
import com.smartcampusmanagmentsystem.service.mapper.MessageMapper;
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
 * Integration tests for the {@link MessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MessageResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_AT = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_SENDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CHAT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CHAT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_CHAT_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_CHAT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BINARY_DATA = "AAAAAAAAAA";
    private static final String UPDATED_BINARY_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_SENDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/messages/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageSearchRepository messageSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Message message;

    private Message insertedMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createEntity() {
        return new Message()
            .content(DEFAULT_CONTENT)
            .createdAt(DEFAULT_CREATED_AT)
            .senderId(DEFAULT_SENDER_ID)
            .contactId(DEFAULT_CONTACT_ID)
            .chatId(DEFAULT_CHAT_ID)
            .groupChatId(DEFAULT_GROUP_CHAT_ID)
            .binaryData(DEFAULT_BINARY_DATA)
            .senderName(DEFAULT_SENDER_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createUpdatedEntity() {
        return new Message()
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .senderId(UPDATED_SENDER_ID)
            .contactId(UPDATED_CONTACT_ID)
            .chatId(UPDATED_CHAT_ID)
            .groupChatId(UPDATED_GROUP_CHAT_ID)
            .binaryData(UPDATED_BINARY_DATA)
            .senderName(UPDATED_SENDER_NAME);
    }

    @BeforeEach
    public void initTest() {
        message = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMessage != null) {
            messageRepository.delete(insertedMessage).block();
            messageSearchRepository.delete(insertedMessage).block();
            insertedMessage = null;
        }
    }

    @Test
    void createMessage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);
        var returnedMessageDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MessageDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Message in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMessage = messageMapper.toEntity(returnedMessageDTO);
        assertMessageUpdatableFieldsEquals(returnedMessage, getPersistedMessage(returnedMessage));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMessage = returnedMessage;
    }

    @Test
    void createMessageWithExistingId() throws Exception {
        // Create the Message with an existing ID
        message.setId("existing_id");
        MessageDTO messageDTO = messageMapper.toDto(message);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllMessages() {
        // Initialize the database
        insertedMessage = messageRepository.save(message).block();

        // Get all the messageList
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
            .value(hasItem(message.getId()))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].senderId")
            .value(hasItem(DEFAULT_SENDER_ID))
            .jsonPath("$.[*].contactId")
            .value(hasItem(DEFAULT_CONTACT_ID))
            .jsonPath("$.[*].chatId")
            .value(hasItem(DEFAULT_CHAT_ID))
            .jsonPath("$.[*].groupChatId")
            .value(hasItem(DEFAULT_GROUP_CHAT_ID))
            .jsonPath("$.[*].binaryData")
            .value(hasItem(DEFAULT_BINARY_DATA))
            .jsonPath("$.[*].senderName")
            .value(hasItem(DEFAULT_SENDER_NAME));
    }

    @Test
    void getMessage() {
        // Initialize the database
        insertedMessage = messageRepository.save(message).block();

        // Get the message
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, message.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(message.getId()))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT))
            .jsonPath("$.senderId")
            .value(is(DEFAULT_SENDER_ID))
            .jsonPath("$.contactId")
            .value(is(DEFAULT_CONTACT_ID))
            .jsonPath("$.chatId")
            .value(is(DEFAULT_CHAT_ID))
            .jsonPath("$.groupChatId")
            .value(is(DEFAULT_GROUP_CHAT_ID))
            .jsonPath("$.binaryData")
            .value(is(DEFAULT_BINARY_DATA))
            .jsonPath("$.senderName")
            .value(is(DEFAULT_SENDER_NAME));
    }

    @Test
    void getNonExistingMessage() {
        // Get the message
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMessage() throws Exception {
        // Initialize the database
        insertedMessage = messageRepository.save(message).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageSearchRepository.save(message).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).block();
        updatedMessage
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .senderId(UPDATED_SENDER_ID)
            .contactId(UPDATED_CONTACT_ID)
            .chatId(UPDATED_CHAT_ID)
            .groupChatId(UPDATED_GROUP_CHAT_ID)
            .binaryData(UPDATED_BINARY_DATA)
            .senderName(UPDATED_SENDER_NAME);
        MessageDTO messageDTO = messageMapper.toDto(updatedMessage);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, messageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMessageToMatchAllProperties(updatedMessage);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Message> messageSearchList = Streamable.of(messageSearchRepository.findAll().collectList().block()).toList();
                Message testMessageSearch = messageSearchList.get(searchDatabaseSizeAfter - 1);

                assertMessageAllPropertiesEquals(testMessageSearch, updatedMessage);
            });
    }

    @Test
    void putNonExistingMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, messageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        insertedMessage = messageRepository.save(message).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        partialUpdatedMessage
            .createdAt(UPDATED_CREATED_AT)
            .groupChatId(UPDATED_GROUP_CHAT_ID)
            .binaryData(UPDATED_BINARY_DATA)
            .senderName(UPDATED_SENDER_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMessage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Message in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMessage, message), getPersistedMessage(message));
    }

    @Test
    void fullUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        insertedMessage = messageRepository.save(message).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        partialUpdatedMessage
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .senderId(UPDATED_SENDER_ID)
            .contactId(UPDATED_CONTACT_ID)
            .chatId(UPDATED_CHAT_ID)
            .groupChatId(UPDATED_GROUP_CHAT_ID)
            .binaryData(UPDATED_BINARY_DATA)
            .senderName(UPDATED_SENDER_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMessage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Message in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageUpdatableFieldsEquals(partialUpdatedMessage, getPersistedMessage(partialUpdatedMessage));
    }

    @Test
    void patchNonExistingMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, messageDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteMessage() {
        // Initialize the database
        insertedMessage = messageRepository.save(message).block();
        messageRepository.save(message).block();
        messageSearchRepository.save(message).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the message
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, message.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(messageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchMessage() {
        // Initialize the database
        insertedMessage = messageRepository.save(message).block();
        messageSearchRepository.save(message).block();

        // Search the message
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + message.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(message.getId()))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT))
            .jsonPath("$.[*].senderId")
            .value(hasItem(DEFAULT_SENDER_ID))
            .jsonPath("$.[*].contactId")
            .value(hasItem(DEFAULT_CONTACT_ID))
            .jsonPath("$.[*].chatId")
            .value(hasItem(DEFAULT_CHAT_ID))
            .jsonPath("$.[*].groupChatId")
            .value(hasItem(DEFAULT_GROUP_CHAT_ID))
            .jsonPath("$.[*].binaryData")
            .value(hasItem(DEFAULT_BINARY_DATA))
            .jsonPath("$.[*].senderName")
            .value(hasItem(DEFAULT_SENDER_NAME));
    }

    protected long getRepositoryCount() {
        return messageRepository.count().block();
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

    protected Message getPersistedMessage(Message message) {
        return messageRepository.findById(message.getId()).block();
    }

    protected void assertPersistedMessageToMatchAllProperties(Message expectedMessage) {
        assertMessageAllPropertiesEquals(expectedMessage, getPersistedMessage(expectedMessage));
    }

    protected void assertPersistedMessageToMatchUpdatableProperties(Message expectedMessage) {
        assertMessageAllUpdatablePropertiesEquals(expectedMessage, getPersistedMessage(expectedMessage));
    }
}
