package com.smartcampusmanagmentsystem.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.GroupChatMembers} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GroupChatMembersDTO implements Serializable {

    private String id;

    private String groupChatId;

    private String memberName;

    private String memberUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberUserId() {
        return memberUserId;
    }

    public void setMemberUserId(String memberUserId) {
        this.memberUserId = memberUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupChatMembersDTO)) {
            return false;
        }

        GroupChatMembersDTO groupChatMembersDTO = (GroupChatMembersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupChatMembersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupChatMembersDTO{" +
            "id='" + getId() + "'" +
            ", groupChatId='" + getGroupChatId() + "'" +
            ", memberName='" + getMemberName() + "'" +
            ", memberUserId='" + getMemberUserId() + "'" +
            "}";
    }
}
