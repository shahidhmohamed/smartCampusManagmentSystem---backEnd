package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.ChatAsserts.*;
import static com.smartcampusmanagmentsystem.domain.ChatTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatMapperTest {

    private ChatMapper chatMapper;

    @BeforeEach
    void setUp() {
        chatMapper = new ChatMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatSample1();
        var actual = chatMapper.toEntity(chatMapper.toDto(expected));
        assertChatAllPropertiesEquals(expected, actual);
    }
}
