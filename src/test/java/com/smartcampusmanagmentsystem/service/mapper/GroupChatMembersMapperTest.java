package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.GroupChatMembersAsserts.*;
import static com.smartcampusmanagmentsystem.domain.GroupChatMembersTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupChatMembersMapperTest {

    private GroupChatMembersMapper groupChatMembersMapper;

    @BeforeEach
    void setUp() {
        groupChatMembersMapper = new GroupChatMembersMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGroupChatMembersSample1();
        var actual = groupChatMembersMapper.toEntity(groupChatMembersMapper.toDto(expected));
        assertGroupChatMembersAllPropertiesEquals(expected, actual);
    }
}
