package com.smartcampusmanagmentsystem.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A GroupChatMembers.
 */
@Document(collection = "group_chat_members")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "groupchatmembers")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GroupChatMembers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("group_chat_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String groupChatId;

    @Field("member_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String memberName;

    @Field("member_user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String memberUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public GroupChatMembers id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupChatId() {
        return this.groupChatId;
    }

    public GroupChatMembers groupChatId(String groupChatId) {
        this.setGroupChatId(groupChatId);
        return this;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public GroupChatMembers memberName(String memberName) {
        this.setMemberName(memberName);
        return this;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberUserId() {
        return this.memberUserId;
    }

    public GroupChatMembers memberUserId(String memberUserId) {
        this.setMemberUserId(memberUserId);
        return this;
    }

    public void setMemberUserId(String memberUserId) {
        this.memberUserId = memberUserId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupChatMembers)) {
            return false;
        }
        return getId() != null && getId().equals(((GroupChatMembers) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupChatMembers{" +
            "id=" + getId() +
            ", groupChatId='" + getGroupChatId() + "'" +
            ", memberName='" + getMemberName() + "'" +
            ", memberUserId='" + getMemberUserId() + "'" +
            "}";
    }
}
