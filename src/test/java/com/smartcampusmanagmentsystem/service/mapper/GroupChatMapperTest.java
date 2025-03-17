package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.GroupChatAsserts.*;
import static com.smartcampusmanagmentsystem.domain.GroupChatTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupChatMapperTest {

    private GroupChatMapper groupChatMapper;

    @BeforeEach
    void setUp() {
        groupChatMapper = new GroupChatMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGroupChatSample1();
        var actual = groupChatMapper.toEntity(groupChatMapper.toDto(expected));
        assertGroupChatAllPropertiesEquals(expected, actual);
    }
}
