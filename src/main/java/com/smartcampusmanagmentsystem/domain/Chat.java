package com.smartcampusmanagmentsystem.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Chat.
 */
@Document(collection = "chat")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "chat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("contact_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contactId;

    @Field("contact")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contact;

    @Field("unread_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String unreadCount;

    @Field("muted")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean muted;

    @Field("title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Field("type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String type;

    @Field("created_at")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdAt;

    @Field("owner")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String owner;

    @Field("owner_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ownerName;

    @Field("binary_data")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String binaryData;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Chat id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactId() {
        return this.contactId;
    }

    public Chat contactId(String contactId) {
        this.setContactId(contactId);
        return this;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContact() {
        return this.contact;
    }

    public Chat contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUnreadCount() {
        return this.unreadCount;
    }

    public Chat unreadCount(String unreadCount) {
        this.setUnreadCount(unreadCount);
        return this;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Boolean getMuted() {
        return this.muted;
    }

    public Chat muted(Boolean muted) {
        this.setMuted(muted);
        return this;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public String getTitle() {
        return this.title;
    }

    public Chat title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public Chat type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public Chat createdAt(String createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOwner() {
        return this.owner;
    }

    public Chat owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public Chat ownerName(String ownerName) {
        this.setOwnerName(ownerName);
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getBinaryData() {
        return this.binaryData;
    }

    public Chat binaryData(String binaryData) {
        this.setBinaryData(binaryData);
        return this;
    }

    public void setBinaryData(String binaryData) {
        this.binaryData = binaryData;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chat)) {
            return false;
        }
        return getId() != null && getId().equals(((Chat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Chat{" +
            "id=" + getId() +
            ", contactId='" + getContactId() + "'" +
            ", contact='" + getContact() + "'" +
            ", unreadCount='" + getUnreadCount() + "'" +
            ", muted='" + getMuted() + "'" +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", owner='" + getOwner() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            ", binaryData='" + getBinaryData() + "'" +
            "}";
    }
}
