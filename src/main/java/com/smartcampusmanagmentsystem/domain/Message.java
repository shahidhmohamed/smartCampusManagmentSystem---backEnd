package com.smartcampusmanagmentsystem.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Message.
 */
@Document(collection = "message")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "message")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("content")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String content;

    @Field("created_at")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdAt;

    @Field("sender_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String senderId;

    @Field("contact_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contactId;

    @Field("chat_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String chatId;

    @Field("group_chat_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String groupChatId;

    @Field("binary_data")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String binaryData;

    @Field("sender_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String senderName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Message id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public Message content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public Message createdAt(String createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public Message senderId(String senderId) {
        this.setSenderId(senderId);
        return this;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContactId() {
        return this.contactId;
    }

    public Message contactId(String contactId) {
        this.setContactId(contactId);
        return this;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getChatId() {
        return this.chatId;
    }

    public Message chatId(String chatId) {
        this.setChatId(chatId);
        return this;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getGroupChatId() {
        return this.groupChatId;
    }

    public Message groupChatId(String groupChatId) {
        this.setGroupChatId(groupChatId);
        return this;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }

    public String getBinaryData() {
        return this.binaryData;
    }

    public Message binaryData(String binaryData) {
        this.setBinaryData(binaryData);
        return this;
    }

    public void setBinaryData(String binaryData) {
        this.binaryData = binaryData;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public Message senderName(String senderName) {
        this.setSenderName(senderName);
        return this;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return getId() != null && getId().equals(((Message) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", senderId='" + getSenderId() + "'" +
            ", contactId='" + getContactId() + "'" +
            ", chatId='" + getChatId() + "'" +
            ", groupChatId='" + getGroupChatId() + "'" +
            ", binaryData='" + getBinaryData() + "'" +
            ", senderName='" + getSenderName() + "'" +
            "}";
    }
}
