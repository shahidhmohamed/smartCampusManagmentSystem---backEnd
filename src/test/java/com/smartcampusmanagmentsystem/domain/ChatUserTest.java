package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.ChatUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatUser.class);
        ChatUser chatUser1 = getChatUserSample1();
        ChatUser chatUser2 = new ChatUser();
        assertThat(chatUser1).isNotEqualTo(chatUser2);

        chatUser2.setId(chatUser1.getId());
        assertThat(chatUser1).isEqualTo(chatUser2);

        chatUser2 = getChatUserSample2();
        assertThat(chatUser1).isNotEqualTo(chatUser2);
    }
}
