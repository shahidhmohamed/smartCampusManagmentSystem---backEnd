package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.GroupChatMembersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GroupChatMembersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupChatMembers.class);
        GroupChatMembers groupChatMembers1 = getGroupChatMembersSample1();
        GroupChatMembers groupChatMembers2 = new GroupChatMembers();
        assertThat(groupChatMembers1).isNotEqualTo(groupChatMembers2);

        groupChatMembers2.setId(groupChatMembers1.getId());
        assertThat(groupChatMembers1).isEqualTo(groupChatMembers2);

        groupChatMembers2 = getGroupChatMembersSample2();
        assertThat(groupChatMembers1).isNotEqualTo(groupChatMembers2);
    }
}
