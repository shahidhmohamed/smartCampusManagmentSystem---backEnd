package com.smartcampusmanagmentsystem.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.GroupChat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GroupChatDTO implements Serializable {

    private String id;

    private String unreadCount;

    private Boolean muted;

    private String title;

    private String type;

    private String createdAt;

    private String owner;

    private String ownerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupChatDTO)) {
            return false;
        }

        GroupChatDTO groupChatDTO = (GroupChatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupChatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupChatDTO{" +
            "id='" + getId() + "'" +
            ", unreadCount='" + getUnreadCount() + "'" +
            ", muted='" + getMuted() + "'" +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", owner='" + getOwner() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            "}";
    }
}
