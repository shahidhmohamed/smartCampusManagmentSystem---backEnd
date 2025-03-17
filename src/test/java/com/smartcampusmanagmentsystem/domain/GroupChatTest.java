package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.GroupChatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GroupChatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupChat.class);
        GroupChat groupChat1 = getGroupChatSample1();
        GroupChat groupChat2 = new GroupChat();
        assertThat(groupChat1).isNotEqualTo(groupChat2);

        groupChat2.setId(groupChat1.getId());
        assertThat(groupChat1).isEqualTo(groupChat2);

        groupChat2 = getGroupChatSample2();
        assertThat(groupChat1).isNotEqualTo(groupChat2);
    }
}
