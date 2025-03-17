package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.ChatUserAsserts.*;
import static com.smartcampusmanagmentsystem.domain.ChatUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatUserMapperTest {

    private ChatUserMapper chatUserMapper;

    @BeforeEach
    void setUp() {
        chatUserMapper = new ChatUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatUserSample1();
        var actual = chatUserMapper.toEntity(chatUserMapper.toDto(expected));
        assertChatUserAllPropertiesEquals(expected, actual);
    }
}
