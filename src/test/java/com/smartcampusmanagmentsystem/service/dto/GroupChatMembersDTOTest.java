package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GroupChatMembersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupChatMembersDTO.class);
        GroupChatMembersDTO groupChatMembersDTO1 = new GroupChatMembersDTO();
        groupChatMembersDTO1.setId("id1");
        GroupChatMembersDTO groupChatMembersDTO2 = new GroupChatMembersDTO();
        assertThat(groupChatMembersDTO1).isNotEqualTo(groupChatMembersDTO2);
        groupChatMembersDTO2.setId(groupChatMembersDTO1.getId());
        assertThat(groupChatMembersDTO1).isEqualTo(groupChatMembersDTO2);
        groupChatMembersDTO2.setId("id2");
        assertThat(groupChatMembersDTO1).isNotEqualTo(groupChatMembersDTO2);
        groupChatMembersDTO1.setId(null);
        assertThat(groupChatMembersDTO1).isNotEqualTo(groupChatMembersDTO2);
    }
}
