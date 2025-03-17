package com.smartcampusmanagmentsystem.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.Chat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatDTO implements Serializable {

    private String id;

    private String contactId;

    private String contact;

    private String unreadCount;

    private Boolean muted;

    private String title;

    private String type;

    private String createdAt;

    private String owner;

    private String ownerName;

    private String binaryData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(String binaryData) {
        this.binaryData = binaryData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatDTO)) {
            return false;
        }

        ChatDTO chatDTO = (ChatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatDTO{" +
            "id='" + getId() + "'" +
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
